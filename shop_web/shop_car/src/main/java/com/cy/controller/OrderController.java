package com.cy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cy.aop.IsLogin;
import com.cy.aop.UserHolder;
import com.cy.entity.Address;
import com.cy.entity.ShopCart;
import com.cy.entity.User;
import com.cy.service.IShopCartService;
import com.cy.service.IUserAddressService;
import com.cy.util.PriceUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "orders")
public class OrderController {

//    @Reference
//    private IOrderService orderService;

    @Reference
    private IShopCartService shopCartService;

    @Reference
    private IUserAddressService userAddressService;


    /**
     * 通过获取到的商品id,获取当前的商品清单
     * 和收获地址并返回商品编辑页面
     * @param gid
     * @param map
     * @return
     */
    @IsLogin(mustLogin = true)
    @RequestMapping(value = "toOrdersEdit")
    public String toOrdersEdit(Integer[] gid, ModelMap map){

        User user = UserHolder.getUser();
        //查询出当前商品的购物车列表
        List<ShopCart> shopCarts = shopCartService.queryCartsByGid(gid, user);
        //根据当前id查询出所有用户地址
        List<Address> addressList = userAddressService.queryAddressByUid(user);

        //计算总价
        double allprice = PriceUtil.allPrice(shopCarts);
        map.put("carts", shopCarts);
        map.put("addresses", addressList);
        map.put("allprice", allprice);

        return "ordersedit";
    }

}
