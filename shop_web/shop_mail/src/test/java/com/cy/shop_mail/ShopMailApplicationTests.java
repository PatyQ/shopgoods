package com.cy.shop_mail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@SpringBootTest
class ShopMailApplicationTests {

    @Autowired
    private JavaMailSender javaMailSender;

    @Test
    void contextLoads() throws MessagingException {

        //创建一封邮件→创建一个email对象
        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();

        //设置为true可以发送附件
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage,true);
        mimeMessageHelper.setSubject("会员到期通知！");
        mimeMessageHelper.setFrom("chauncyq@sina.com");
        mimeMessageHelper.setTo("859832098@qq.com");
        mimeMailMessage.setText("会员到期通知！请及时充值");

        //可以发送附件
//        mimeMessageHelper.addAttachment("",new File());
        mimeMessageHelper.setSentDate(new Date());

        javaMailSender.send(mimeMailMessage);
    }

}
