package com.cy.shop_goods;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean   //new一个交换机
    public FanoutExchange getExchange(){
        return new FanoutExchange("goods_exchange");
    }
    //交换机队列的绑定有两种写法
}
