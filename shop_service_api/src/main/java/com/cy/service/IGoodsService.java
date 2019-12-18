package com.cy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cy.entity.Goods;
import com.cy.entity.GoodsImages;

import java.util.List;

public interface IGoodsService {

    IPage<Goods> pagegetGoodsPage(Page<Goods> page);

    Integer addGoods(Goods goods);

    List<GoodsImages> selGoodsImages();

    Goods selGoodsById(Integer id);

//    String addCartGoods(ShopCart shopCart, User user, String cartToken);

}
