package com.cy.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cy.entity.Goods;
import com.cy.service.ISearchService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements ISearchService {


    @Autowired
    private SolrClient solrClient;

    @Override
    public int insertSolr(Goods goods) {

        SolrInputDocument document = new SolrInputDocument();

        document.addField("id", goods.getId() + "");
//        document.addField("id", goods.getId());
        document.addField("subject", goods.getSubject());
        document.addField("info", goods.getInfo());
//        BigDecimal会自动调用tostring方法,需要将它转换为double类型
        document.addField("price", goods.getPrice().doubleValue());
        document.addField("save", goods.getSave());
        document.addField("image", goods.getFmurl());

        try {
            //将???????
            solrClient.add(document);
            //成功提交并返回1
            solrClient.commit();
            return 1;
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //失败返回0
        return 0;
    }


    /**
     * 根据当前输入信息搜索
     * @param keyword
     * @return
     */
    @Override
    public List<Goods> querySolr(String keyword) {

        SolrQuery solrQuery = new SolrQuery();
        if (keyword != null && !keyword.equals("")){
//            查找索引库
            solrQuery.setQuery("subject:" + keyword + " || info:" + keyword);
        }else {
            solrQuery.set("*:*");
        }


        //进行分页的设置 limit 0,10
        solrQuery.setStart(0);
        solrQuery.setRows(10);
        //设置搜索的高亮
        solrQuery.setHighlight(true);//开启高亮
        solrQuery.setHighlightSimplePre("<font color='red'>");//设置搜素前缀
        solrQuery.setHighlightSimplePost("</font>");//设置搜索后缀
        solrQuery.addHighlightField("subject");//设置哪些字段会有高亮

        //高亮的结果要单独的获取
        try {
            List<Goods> goodsList = new ArrayList<>();
            QueryResponse query = solrClient.query(solrQuery);

            //获得高亮的结果
            //结构:
                //Map<id, 高亮信息>
                //高亮信息 -   Map<字段, 高亮内容的集合>
            Map<String, Map<String, List<String>>> highlighting = query.getHighlighting();


            //获得搜索的结果
            SolrDocumentList results = query.getResults();
            for (SolrDocument r : results) {
                Goods goods = new Goods();
//                ================未完待续===================
                goods.setId(Integer.parseInt((String) r.get("id")));
                goods.setSubject(r.get("subject")+"");
                goods.setSave((int)r.get("save"));
                goods.setPrice(BigDecimal.valueOf((double)r.get("price")));
                goods.setFmurl(r.get("image")+"");
                goodsList.add(goods);

//                ---------------高亮分隔-----------------
                //处理高亮结果
                //如果为true则有高亮字段
                if(highlighting.containsKey(goods.getId() + "")){
                    Map<String, List<String>> stringListMap = highlighting.get(goods.getId() + "");
                    List<String> subject = stringListMap.get("subject");
                    if(subject != null){
                        goods.setSubject(subject.get(0));
                    }
                }
            }

            System.out.println("searchServiceImpl返回的内容为: "+goodsList);
            return goodsList;
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
