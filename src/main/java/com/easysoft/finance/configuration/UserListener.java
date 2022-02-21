/*
 * Copyright (c) 2021.
 */

package com.easysoft.finance.configuration;

import com.easysoft.finance.configuration.event.UserForgetPasswordEvent;
import com.easysoft.finance.configuration.event.UserRegistrationEvent;
import com.easysoft.finance.configuration.event.UserResendActivationCodeEvent;
import com.easysoft.finance.domain.User;
import com.easysoft.finance.repository.UserRepository;
import com.easysoft.finance.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class UserListener {

    @Autowired
    public EmailService emailService;
    @Autowired
    UserRepository userRepository;

    /*Listener to receive the event returned by Spring*/
    @Async
    @EventListener
    void handleUserRegistrationEvent(UserRegistrationEvent event) {
        User user = (User) event.getSource();
        emailService.sendTextMail(user.getUsername(), "Account Activation Code", "Security Activation Code: " + user.getActivationCode());
    }

    @Async
    @EventListener
    void handleUserResendActivationCodeEvent(UserResendActivationCodeEvent event) {
        User user = (User) event.getSource();
        emailService.sendTextMail(user.getUsername(), "Resend Activation Code", "Security Activation Code: " + user.getActivationCode());
    }

    @Async
    @EventListener
    void handleUserForgetPasswordEvent(UserForgetPasswordEvent event) {
        User user = (User) event.getSource();
        emailService.sendTextMail(user.getUsername(), "Forget Password", "Your Temporary Password: " + event.getTempPassword());
    }

}
