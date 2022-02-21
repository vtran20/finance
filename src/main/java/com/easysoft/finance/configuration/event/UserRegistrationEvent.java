/*
 * Copyright (c) 2021.
 */

package com.easysoft.finance.configuration.event;

import org.springframework.context.ApplicationEvent;

public class UserRegistrationEvent extends ApplicationEvent {
    private static final long serialVersionUID = 1L;
    
    public UserRegistrationEvent(Object source) {
        super(source);
    }
}
