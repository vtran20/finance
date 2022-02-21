/*
 * Copyright (c) 2021.
 */

package com.easysoft.finance.configuration.event;

import org.springframework.context.ApplicationEvent;

public class UserForgetPasswordEvent extends ApplicationEvent {
    private static final long serialVersionUID = 1L;

    private String tempPassword;

    public UserForgetPasswordEvent(Object source, String tempPassword) {
        super(source);
        this.tempPassword = tempPassword;
    }

    public String getTempPassword() {
        return tempPassword;
    }


}
