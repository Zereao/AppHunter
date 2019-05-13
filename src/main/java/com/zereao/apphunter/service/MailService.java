package com.zereao.apphunter.service;

import com.zereao.apphunter.common.builder.MailMessageBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

/**
 * @author Zereao
 * @version 2019/05/11 12:15
 */
@Service
public class MailService {
    @Resource
    private JavaMailSender mailSender;

    @Value("${mail.sender}")
    private String sender;
    @Value("#{'${mail.to}'.split(',')}")
    private String[] recipient;
    @Value("${mail.subject}")
    private String subject;

    /**
     * 发送普通文本邮件
     *
     * @param content 邮件内容
     */
    public void sendMail(String content) {
        SimpleMailMessage message = new MailMessageBuilder()
                .from(sender)
                .to(recipient)
                .subject(subject)
                .text(content)
                .buildSimpleMailMessage();
        mailSender.send(message);
    }

    /**
     * 发送HTML 邮件
     *
     * @param htmlContent HTML 内容
     */
    public void sendHtmlMail(String htmlContent) {
        MimeMessage message = mailSender.createMimeMessage();
        new MailMessageBuilder()
                .from(sender)
                .to(recipient)
                .subject(subject)
                .text(htmlContent)
                .buildMimeMailMessage(message);
        mailSender.send(message);
    }
}
