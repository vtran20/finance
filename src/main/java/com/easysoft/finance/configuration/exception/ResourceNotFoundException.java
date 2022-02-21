/*
 * Copyright (c) 2021.
 */

package com.easysoft.finance.configuration.exception;

import com.easysoft.finance.configuration.exception.base.BaseException;

public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String title, String message, String errorCode, String debugMessage) {
        super(title, message, errorCode, debugMessage);
    }
}