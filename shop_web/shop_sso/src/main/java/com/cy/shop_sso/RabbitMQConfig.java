package com.cy.shop_sso;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public FanoutExchange getExchange(){
        return new FanoutExchange("mail_exchange");
    }

    @Bean   //queue 队列
    public Queue getQueue (){
        return new Queue("mail_queue");
    }

    @Bean   //Builder 构造器    //将队列绑定上交换机
    public Binding getBinding(Queue getQueue, FanoutExchange getExchange){
        return BindingBuilder.bind(getQueue).to(getExchange);
    }

}
