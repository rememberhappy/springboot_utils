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

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 邮件发送
 */
@Service
public class SendMailService {

    private final static Logger logger = LoggerFactory.getLogger(SendMailService.class);

    @Autowired
    private TemplateEngine templateEngine;
    // 需要在配置文件中 配置模板信息。否则启动不了
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:asd}")
    private String username;

    /**
     * @param email    邮箱账号
     * @param signName 标志名称
     * @param subject  主体
     * @param template 生产html文件的名称
     * @param authCode 生成的验证码
     * @return boolean
     * @Throws
     * @Author zhangdj
     * @date 2021/6/15 16:05
     */
    public boolean send(String email, String signName, String subject, String template, String authCode) {
        Map<String, Object> param = new HashMap<>();
        param.put("signName", signName);
        param.put("authCode", authCode);
        Context context = new Context();
        Iterator<String> it = param.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            context.setVariable(key, param.get(key));
        }
        // 使用thymeleaf模版来生产html文件。
        String emailContent = templateEngine.process(template, context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(username);
            helper.setTo(email);
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
