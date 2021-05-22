package com.springbootutils.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Iterator;
import java.util.Map;

@Service
public class SendMailService {

    Logger logger = LoggerFactory.getLogger(SendMailService.class);

    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;
    public boolean send(String to, String subject, String template, Map<String, Object> param){
        Context context = new Context();
        Iterator<String> it = param.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            context.setVariable(key, param.get(key));
        }
        String emailContent = templateEngine.process(template, context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(username);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(emailContent, true);
        } catch (MessagingException e) {
            logger.error("发送邮件异常:{}", e);
            return false;
        }
        mailSender.send(message);
        return true;
    }
}
