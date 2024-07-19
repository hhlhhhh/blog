package com.blog.back.utils;

import com.blog.back.framework.enums.EmailCodeType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SendEmailUtil {

    @Resource
    private JavaMailSender sender;

    @Value("${blog.email.from}")
    private String from;


    public void sendMail(String to, String code, EmailCodeType type){
        String content="尊敬的用户您好，您的"+type.getName()+"验证码是"+code+"，验证码"+type.getTime()+"分钟内有效。如不是本人操作，请忽略此邮件！";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("【2hlblog-mail】注册验证码");
        message.setText(content);
        message.setTo(to);
        message.setFrom(from);
        sender.send(message);
    }
}
