package com.cy.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cy.dao.IGoodsDao;
import com.cy.dao.IGoodsImagesDao;
import com.cy.entity.Goods;
import com.cy.entity.GoodsImages;
import com.cy.service.IGoodsService;
import com.cy.service.ISearchService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class GoodsServiceImpl implements IGoodsService {

    @Autowired
    private IGoodsDao goodsDao;

//    成为消费者调用searchService
    @Reference
    private ISearchService searchService;

    @Autowired
    private IGoodsImagesDao goodsImagesDao;

    @Autowired      //template : 模板
    private RabbitTemplate rabbitTemplate;


    @Override
    public IPage<Goods> pagegetGoodsPage(Page<Goods> page){
        // 不进行 count sql 优化，解决 MP 无法自动优化 SQL 问题，这时候你需要自己查询 count 部分
        // page.setOptimizeCountSql(false);
        // 当 total 为小于 0 或者设置 setSearchCount(false) 分页插件不会进行 count 查询
        // 要点!! 分页返回的对象与传入的对象是同一个
        return goodsDao.selectPageVo(page);
    }

    /**
     * 添加商品
     * @param goods
     * @return
     */
    @Override
    public Integer addGoods(Goods goods) {
        Goods goods1 = new Goods();
        goods1.setSubject(goods.getSubject());
        goods1.setFmurl(goods.getFmurl());
        goods1.setInfo(goods.getInfo());
        goods1.setPrice(goods.getPrice());
        goods1.setSave(goods.getSave());
        goods1.setCreateTime(goods.getCreateTime());
        int insert = goodsDao.insert(goods);
        goods1.setId(goods.getId());

        GoodsImages goodsImages2 = new GoodsImages();
        goodsImages2.setGid(goods.getId());
        goodsImages2.setInfo(goods1.getInfo());
        goodsImages2.setIsfengmian(1);
        goodsImages2.setUrl(goods1.getFmurl());
        goodsImagesDao.insert(goodsImages2);

        List<String> otherurl = goods.getOtherurl();
        for (String s : otherurl) {
            GoodsImages goodsImages = new GoodsImages();
            goodsImages.setGid(goods.getId());
            goodsImages.setInfo(goods1.getInfo());
            goodsImages.setIsfengmian(0);
            goodsImages.setUrl(s);
            goodsImagesDao.insert(goodsImages);
        }

        //将商品信息同步到索引库
        searchService.insertSolr(goods1);
        //  提供者步骤
//        rabbitTemplate.convertAndSend("goods_exchange",goods1);

        return insert;
    }

    @Override
    public List<GoodsImages> selGoodsImages() {
        List<GoodsImages> goodsImages1 = goodsImagesDao.selectList(null);
        return goodsImages1;
    }

    @Override
    public Goods selGoodsById(Integer id) {

        return goodsDao.selGoodsById(id);
    }

}

