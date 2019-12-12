package com.cy.lister;

import com.cy.entity.Goods;
import com.cy.service.ISearchService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component  //把普通pojo实例化到spring容器中，相当于配置文件中的<bean id="" class=""/>
public class SearchLister {

    @Autowired
    private ISearchService searchService;

    //做监听器同时做一个队列的绑定,绑定一个交换机
    @RabbitListener(bindings =@QueueBinding( exchange = @Exchange(value = "goods_exchange", type = "fanout"),value = @Queue(name = "search_queue")))
    public void msgHandler(Goods goods){
        searchService.insertSolr(goods);
    }

}
