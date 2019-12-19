package com.cy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.cy.aop.IsLogin;
import com.cy.aop.UserHolder;
import com.cy.entity.ShopCart;
import com.cy.entity.User;
import com.cy.service.IGoodsService;
import com.cy.service.IShopCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("car")
public class CarController {

    @Reference
    private IGoodsService goodsService;
    @Reference
    private IShopCartService shopCartService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 添加购物车
     * @param gid
     * @param gnumber
     * @param cartToken
     * @param response
     * @return
     */
    @RequestMapping("addcat")
    @IsLogin
    public String addCar(Integer gid, Integer gnumber, @CookieValue(name = "cartToken", required = false) String cartToken
            , HttpServletResponse response) {
        System.out.println("商品ID" + gid + "商品数量" + gnumber);
        System.out.println("获得当前用户" + UserHolder.getUser());

        //根据用户判断当前用户是否登录
        User user = UserHolder.getUser();

        ShopCart shopCart = new ShopCart();
//        Goods goods = goodsService.selGoodsById(gid);//写入servers层
//        shopCart.setGoods(goods);
        shopCart.setGid(gid);
        shopCart.setNumber(gnumber);

        if (user!=null){
            Integer id = user.getById();//获取不到id,只能使用备用ID
            shopCart.setUid(id);
        }

        //用户已登录,将购物车信息添加到数据库中,返回购物车的token
        cartToken = shopCartService.addCartGoods(shopCart, user, cartToken);
        //购物车的token写入cookie中
        Cookie cookie = new Cookie("cartToken", cartToken);
        cookie.setMaxAge(60 * 60 * 24 * 365);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "successcart";
    }

    /**
     * 添加小购物车
     * @param cartToken
     * @param callback
     * @return
     */
    @RequestMapping("smallcat")
    @IsLogin
    @ResponseBody
    public String showSmallcat(@CookieValue(name = "cartToken", required = false) String cartToken,String callback){

        User user = UserHolder.getUser();
        if (user!=null){//已登录,将存入数据库中的购物车取出
            user.setId(user.getById());
        }//在service中判断用户是否登录---未验证是否能够获取到值
//        asljg;dfjkagjklagjaksl
        List<ShopCart> shopCarts = shopCartService.selCarts(cartToken, user);

        return callback != null ? callback + "(" + JSON.toJSONString(shopCarts) + ")" : JSON.toJSONString(shopCarts);
    }

    /**
     * 将redis中的购物车信息合并
     * @param cartToken
     * @param returnUrl
     * @return
     */
    @RequestMapping("merge")
    @IsLogin(mustLogin = true)
    public String mergeCart(@CookieValue(name="cartToken",required = false) String cartToken
            ,String returnUrl,HttpServletResponse response){

        if (cartToken != null){
            //获取到redis中的购物车
            Long size = redisTemplate.opsForList().size(cartToken);
            List<ShopCart> shopCarts = redisTemplate.opsForList().range(cartToken, 0, size);

            //将redis中的购物车添加到数据库中并清空redis中当前用户的购物车信息
            User user = UserHolder.getUser();

            for (ShopCart cart : shopCarts) {
                cart.setUid(user.getById());
                shopCartService.addCartGoods(cart,user,cartToken);
            }

            //删除当前用户的redis购物车并删除cookie
            Boolean delete = redisTemplate.delete(cartToken);

            Cookie cookie = new Cookie("cartToken",null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

        return "redirect:"+ returnUrl;
    }


    /**
     * 跳转到购物车登录页面
     * @param cartToken
     * @param map
     * @return
     */
    @IsLogin
    @RequestMapping(value = "showShopCarts")
    public String showShopCarts(@CookieValue(name = "cartToken",required = false)String cartToken,
                                ModelMap map){

        User user = UserHolder.getUser();
        List<ShopCart> shopCarts = null;
        if (user != null){//已登录
            user.setId(user.getById());
            shopCarts = shopCartService.selCarts(cartToken, user);

        }else {//未登录
            Long size = redisTemplate.opsForList().size(cartToken);
            shopCarts = redisTemplate.opsForList().range(cartToken, 0, size);
        }
        map.put("carts",shopCarts);
        return "cartlist";
    }



}
