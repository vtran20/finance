/*
 * Copyright (c) 2021.
 */

package com.easysoft.finance.configuration.event;

import org.springframework.context.ApplicationEvent;

public class UserResendActivationCodeEvent extends ApplicationEvent {
    private static final long serialVersionUID = 1L;

    public UserResendActivationCodeEvent(Object source) {
        super(source);
    }
}
