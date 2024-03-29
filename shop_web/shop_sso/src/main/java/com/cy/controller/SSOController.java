package com.cy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cy.entity.User;
import com.cy.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("sso")
public class SSOController {

    @Reference
    private IUserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("gologin")
    public String goLogin (){
        return "login";
    }

    @RequestMapping("goregist")
    public String goRegister (){
        return "register";
    }

    @RequestMapping("register")
    public String userRegister(User user){
        System.out.println("接收到的用户注册信息: "+user);
        int i = userService.userRegist(user);
        if (i>0){
            return "login";
        }else {
            return "register";
        }
    }

    @RequestMapping("goforget")
    public String goforget(){
        return "forgetpwd";
    }

    /**
     * 前台发送登录请求,接收用户名密码,验证是否正确,将登录信息存入redis
     *
     * @return
     */
    @RequestMapping(value = "login")
    public String login(String username, String password, HttpServletResponse response){
        User user = userService.selUserByName(username);
        if (user==null){
            return "login";
        }
        String pwd = user.getPassword();
        if (!pwd.equals(password)){
            return "login";
        }
        //======================用户存在且密码验证正确==============================//
        String uuid = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(uuid,user);
        redisTemplate.expire(uuid,10,TimeUnit.MINUTES);

        //将token写入浏览器的cookie中→配置cookie的各种参数
        Cookie cookie = new Cookie("loginToken",uuid);
        cookie.setMaxAge(60*60*5);
        cookie.setDomain("localhost");//域名
        cookie.setPath("/");

        response.addCookie(cookie);
        return "redirect:http://localhost:8889";
    }


}
