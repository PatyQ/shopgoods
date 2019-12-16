package com.cy.controller;

import com.cy.aop.IsLogin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("car")
public class CarController {

    @RequestMapping("addcat")
    @IsLogin(mustLogin = true)
    public String addCar(Integer gid,Integer gnumber){
        System.out.println("商品ID"+gid+"商品数量"+gnumber);

        return "showcar";
    }
}
