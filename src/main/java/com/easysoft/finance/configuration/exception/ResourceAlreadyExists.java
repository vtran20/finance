/*
 * Copyright (c) 2021.
 */

package com.easysoft.finance.configuration.exception;

import com.easysoft.finance.configuration.exception.base.BaseException;

public class ResourceAlreadyExists extends BaseException {

    public ResourceAlreadyExists(String message) {
        super(message);
    }

    public ResourceAlreadyExists(String title, String message, String errorCode, String debugMessage) {
        super(title, message, errorCode, debugMessage);
    }
}
