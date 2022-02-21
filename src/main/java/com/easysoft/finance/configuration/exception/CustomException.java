/*
 * Copyright (c) 2021.
 */

package com.easysoft.finance.configuration.exception;

import com.easysoft.finance.configuration.exception.base.BaseException;

public class CustomException extends BaseException {

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String title, String message, String errorCode, String debugMessage) {
        super(title, message, errorCode, debugMessage);
    }
}