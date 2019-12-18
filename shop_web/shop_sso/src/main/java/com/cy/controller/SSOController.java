package com.cy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.cy.entity.ResultData;
import com.cy.entity.User;
import com.cy.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String login(String username, String password, String returnUrl, HttpServletResponse response){
        User user = userService.selUserByName(username);
        Integer id = user.getId();
        if (user==null){
            return "login";
        }
        String pwd = user.getPassword();
        if (!pwd.equals(password)){
            return "login";
        }
        //======================用户存在且密码验证正确==============================//
        //将备用id放进去
        user.setById(user.getId());
        String uuid = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(uuid,user);
        redisTemplate.expire(uuid,20,TimeUnit.MINUTES);

        //将token写入浏览器的cookie中→配置cookie的各种参数
        Cookie cookie = new Cookie("loginToken",uuid);
        cookie.setMaxAge(60*60*5);
        cookie.setDomain("localhost");//域名
        cookie.setPath("/");

        System.out.println("将要返回的页面"+returnUrl);
        response.addCookie(cookie);
        if (returnUrl==null || returnUrl.equals("")){
            return "redirect:http://localhost:8889";
        }else {
            return "redirect:"+returnUrl;
        }
    }

    /**
     * 判断是否登录
     * @param loginToken
     * @param callback
     * @return
     */
    @RequestMapping(value = "islogin")
    @ResponseBody
    //jsonp格式需要,gosn拼接callback返回
    public String islogin(@CookieValue(value = "loginToken" ,required = false) String loginToken, String callback){
        System.out.println("获取到的token值"+loginToken);
        System.out.println("获取到的callback值"+callback);//获取到的token值null
        ResultData resultData = new ResultData();
//        Gson gson = new Gson();
        if (loginToken!=null){
            User user = (User) redisTemplate.opsForValue().get(loginToken);
            if (user!=null){
                resultData.setCode("200").setMsg("成功").setData(user);
            }
        }else {
            resultData.setCode("500");
        }
        //可以判断callback是否为空,空则为ajax请求
        return callback+"("+ JSON.toJSONString(resultData) +")";
    }

    /**
     * 注销用户返回登录首页
     *删除redis中的令牌,移除cookie
     */
    @RequestMapping(value = "logout")
    public String userLogout(@CookieValue(name = "loginToken",required=false)String loginToken,HttpServletResponse response){
        redisTemplate.delete(loginToken);
        Cookie cookie = new Cookie("loginToken",null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:http://localhost:8889/";
    }

    /**
     * 注册用户
     */
    @RequestMapping(value = "toregist")
    public String toregist(){
        return "redirect:http://localhost:8889/";
    }

}
