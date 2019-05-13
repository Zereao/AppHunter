package com.zereao.apphunter.common.builder;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author Zereao
 * @version 2019/05/11 12:45
 */
@Slf4j
public class MailMessageBuilder {
    private String from;
    private String replyTo;
    private String[] to;
    private String[] cc;
    private String[] bcc;
    private Date sentDate;
    private String subject;
    private String text;


    public SimpleMailMessage buildSimpleMailMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setReplyTo(replyTo);
        message.setTo(to);
        message.setCc(cc);
        message.setBcc(bcc);
        message.setSentDate(sentDate);
        message.setSubject(subject);
        message.setText(text);
        return message;
    }

    public void buildMimeMailMessage(MimeMessage mimeMessage) {
        MimeMessageHelper mimeMessageHelper = null;
        //true表示需要创建一个multipart message
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(from);
            if (!StringUtils.isEmpty(replyTo)) {
                mimeMessageHelper.setReplyTo(replyTo);
            }
            if (!ArrayUtils.isEmpty(to)) {
                mimeMessageHelper.setTo(to);
            }
            if (!ArrayUtils.isEmpty(cc)) {
                mimeMessageHelper.setCc(cc);
            }
            if (!ArrayUtils.isEmpty(bcc)) {
                mimeMessageHelper.setBcc(bcc);
            }
            if (sentDate != null) {
                mimeMessageHelper.setSentDate(sentDate);
            }
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text, true);
        } catch (MessagingException e) {
            log.error("创建 multipart MimeMessageHelper 失败！", e);
        }
    }


    public MailMessageBuilder from(String from) {
        this.from = from;
        return this;
    }

    public MailMessageBuilder replyTo(String replyTo) {
        this.replyTo = replyTo;
        return this;
    }

    public MailMessageBuilder to(String... to) {
        this.to = to;
        return this;
    }

    public MailMessageBuilder cc(String... cc) {
        this.cc = cc;
        return this;
    }

    public MailMessageBuilder bcc(String... bcc) {
        this.bcc = bcc;
        return this;
    }

    public MailMessageBuilder sentDate(Date sentDate) {
        this.sentDate = sentDate;
        return this;
    }

    public MailMessageBuilder subject(String subject) {
        try {
            this.subject = MimeUtility.encodeText(subject, MimeUtility.mimeCharset(StandardCharsets.ISO_8859_1.name()), null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this;
    }

    public MailMessageBuilder text(String text) {
        this.text = text;
        return this;
    }
}
