package com.cy.shop_searchservice;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.cy")
@DubboComponentScan("com.cy.service.impl")
public class ShopSearchserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopSearchserviceApplication.class, args);
    }

}
