package com.cy.service;

import com.cy.entity.ShopCart;
import com.cy.entity.User;

import java.util.List;

public interface IShopCartService {

    String addCartGoods(ShopCart shopCart, User user, String cartToken);

    List<ShopCart> selCarts(String cartToken, User user);

    List<ShopCart> queryCartsByGid(Integer[] gid, User user);
}
