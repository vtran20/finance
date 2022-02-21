/*
 * Copyright (c) 2021.
 */

package com.easysoft.finance.configuration.exception.base;

public class BaseException extends RuntimeException {
    
    private String title;
    private String errorCode;
    private String debugMessage;

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String title, String message, String errorCode, String debugMessage) {
        super(message);
        this.title = title;
        this.errorCode = errorCode;
        this.debugMessage = debugMessage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }
}
