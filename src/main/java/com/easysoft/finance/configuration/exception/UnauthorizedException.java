/*
 * Copyright (c) 2021.
 */

package com.easysoft.finance.configuration.exception;

import com.easysoft.finance.configuration.exception.base.BaseException;

public class UnauthorizedException extends BaseException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String title, String message, String errorCode, String debugMessage) {
        super(title, message, errorCode, debugMessage);
    }
}