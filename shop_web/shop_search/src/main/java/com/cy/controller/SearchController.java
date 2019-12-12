package com.cy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cy.entity.Goods;
import com.cy.service.ISearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "search")
public class SearchController {

    @Reference
    private ISearchService searchService;


    @RequestMapping("searchByKeyword")
    public String searchByKeyword(String keyWord, ModelMap map) {
        System.out.println("搜索的关键字: " + keyWord);

        List<Goods> goodsList = searchService.querySolr(keyWord);

        System.out.println(goodsList);
        map.put("goodsList", goodsList);
        return "searchlist";
    }
}
