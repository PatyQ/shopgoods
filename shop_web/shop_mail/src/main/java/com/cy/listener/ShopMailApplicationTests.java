package com.cy.listener;

import com.cy.entity.Email;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class ShopMailApplicationTests {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String form;

    // Declare : 声明
    @RabbitListener(queuesToDeclare = @Queue(name="mail_queue"))
    public void msgHandler(Email email) throws MessagingException {
        //创建一封邮件
        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
        //spring提供的邮件设置对象
        MimeMessageHelper mmh = new MimeMessageHelper(mimeMailMessage,true);

        mmh.setSubject(email.getSubject());
        mmh.setFrom(form);
        mmh.setTo(email.getTo());
        mmh.setText(email.getContext(),true);
        mmh.setSentDate(email.getSendTime());

        //发送邮件
        javaMailSender.send(mimeMailMessage);
        System.out.println("发送邮件测试完成!");
    }

}
