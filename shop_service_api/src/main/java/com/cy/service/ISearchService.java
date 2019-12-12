package com.cy.service;

import com.cy.entity.Goods;

import java.util.List;

public interface ISearchService {

    int insertSolr (Goods goods);

    /**
     * 根据关键字查询索引库
     * @return
     */
    List<Goods> querySolr(String keyWord);
}
