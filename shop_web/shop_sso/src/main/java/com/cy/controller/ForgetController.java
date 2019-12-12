package com.cy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cy.entity.Email;
import com.cy.entity.ResultData;
import com.cy.entity.User;
import com.cy.service.IUserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("forget")
public class ForgetController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Reference
    private IUserService userService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送找回密码的邮件
     * @param username
     * @return
     */
    @RequestMapping(value = "forgetpwd")
    @ResponseBody
    public ResultData<Map<String,String>> forgetpwd(String username){
        System.out.println(username);
        //去数据库中查找该用户信息
        User user = userService.selUserByName(username);

        if (user==null){
            return new ResultData<>("500","邮件发送失败,查无此人",null);
        }

        //================用户名不为空,可以发送邮件=====================//

        String uuid = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(uuid,user.getUsername());
        redisTemplate.expire(uuid,5, TimeUnit.MINUTES);

        String url = "http://localhost:8892/forget/toUpdatePassword?token=" + uuid;

        Email email = new Email();
        email.setTo(user.getEmail());
        email.setSubject("找回密码,是本人操作请重视!");
        email.setContext(url);
        email.setSendTime(new Date());

        rabbitTemplate.convertAndSend("mail_exchange","",email);

        //存在这个用户,向这个用户中发送邮件
        return new ResultData<>("200","邮件发送成功",null);
    }


    @RequestMapping(value = "toUpdatePassword")
    public String toUpdatePassword(){
        //=============== 保持原网页转发 =====================//
        return "updatepassword";
    }

    @RequestMapping(value = "updPwd")
    public String updPwd(String newpassword,String token){
        System.out.println(token);
        System.out.println(newpassword);
        //================与redis对比======================//
        //序列化与string
        String s = (String) redisTemplate.opsForValue().get(token);
        if (s!=null){
            System.out.println("假的修改密码成功");
        }
        return "login";
    }

}
