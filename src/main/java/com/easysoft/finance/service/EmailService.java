package com.easysoft.finance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Created by vutran on 9/27/2017.
 */
@Component
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendTextMail(String subject, String body) throws MailException {

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo("vu.t.tran@oracle.com");
        mail.setFrom("vuktx1979@gmail.com");
        mail.setSubject(subject);
        mail.setText(body);
        javaMailSender.send(mail);
    }
}
