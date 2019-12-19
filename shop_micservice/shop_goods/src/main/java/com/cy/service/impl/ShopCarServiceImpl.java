package com.cy.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cy.dao.IShopCartDao;
import com.cy.entity.Goods;
import com.cy.entity.ShopCart;
import com.cy.entity.User;
import com.cy.service.IGoodsService;
import com.cy.service.IShopCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class ShopCarServiceImpl implements IShopCartService {

    @Autowired
    private IShopCartDao ShopCartDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Reference
    private IGoodsService goodsService;

    @Override
    public String addCartGoods(ShopCart shopCart, User user, String cartToken) {

        Goods goods = goodsService.selGoodsById(shopCart.getGid());
        ShopCart cart = shopCart.setGoods(goods);////multiply:乘
        //计算小计--空指针异常标记
        BigDecimal bigDecimal = cart.getGoods().getPrice().multiply(BigDecimal.valueOf(shopCart.getNumber()));
        shopCart.setCartPrice(bigDecimal);//将小计放入购物车中

        if (user!=null){
//            Integer id = user.getById();//获取不到id,只能使用备用ID
//            shopCart.setUid(id);

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("gid",shopCart.getGid());
            queryWrapper.eq("uid",shopCart.getUid());

            //根据gid查看购物车中是否存在此商品,如存在则数量+,否则将购物车添加进数据库
            ShopCart selectOne = ShopCartDao.selectOne(queryWrapper);
            if (selectOne != null){
                Integer goodsnum = selectOne.getNumber();
                BigDecimal cartPrice = selectOne.getCartPrice();//获得数据库中购物车价格
                goodsnum = goodsnum + shopCart.getNumber();
                cartPrice = cartPrice.add(shopCart.getGoods().getPrice());//将当前价格加上购物车中的价格
                selectOne.setNumber(goodsnum);
                selectOne.setCartPrice(cartPrice);
                int updateById = ShopCartDao.updateById(selectOne);
            }else {//数据库中没有,直接添加
                int insert = ShopCartDao.insert(shopCart);
            }

        }else { //如果用户未登录,则将购物车存储在cookie中
//            cartToken = cartToken != null ? cartToken : UUID.randomUUID().toString();
//            cartToken = cartToken != null ? cartToken : UUID.randomUUID().toString();
            if (cartToken == null || cartToken.equals("")){
                cartToken =  UUID.randomUUID().toString();
            }
            redisTemplate.opsForList().leftPush(cartToken,shopCart);
        }

        return cartToken;
    }


    /**
     * 查询购物车中的所有信息--已登录,未登录
     * @param cartToken
     * @param user
     * @return
     */
    @Override
    public List<ShopCart> selCarts(String cartToken, User user) {
        List<ShopCart> shopCarts = null;
        if (user != null){//用户已登录,直接从数据库中取值
            int byId = user.getById();
            QueryWrapper<ShopCart> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uid",byId);
            queryWrapper.orderByDesc("create_time");
            shopCarts = ShopCartDao.selectList(queryWrapper);

        }else {//用户未登录,将redis中的购物车取出
            if (cartToken != null){
                //获得redis中购物车的长度
                Long size = redisTemplate.opsForList().size(cartToken);
                //查询list中指定范围的内容
                shopCarts = redisTemplate.opsForList().range(cartToken, 0, size);
            }
        }

        //关联查询所有购物车的商品信息，方便页面展示
        if (shopCarts != null){
            for (ShopCart shopCart : shopCarts) {
                Integer gid = shopCart.getGid();
                Goods goods = goodsService.selGoodsById(gid);
                shopCart.setGoods(goods);
            }
        }

        return shopCarts;
    }
}
