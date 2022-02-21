/*
 * Copyright (c) 2021.
 */

package com.easysoft.finance.configuration.exception;

import com.easysoft.finance.configuration.exception.base.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> resourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(HttpStatus.NOT_FOUND);
        response.setErrorCode("NOT_FOUND");
        response.setTitle(ex.getTitle());
        response.setMessage(ex.getMessage());
        response.setUri(((ServletWebRequest) request).getRequest().getRequestURI());
        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExists.class)
    public ResponseEntity<ExceptionResponse> resourceAlreadyExists(ResourceAlreadyExists ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(HttpStatus.CONFLICT);
        response.setErrorCode("CONFLICT");
        response.setTitle(ex.getTitle());
        response.setMessage(ex.getMessage());
        response.setUri(((ServletWebRequest) request).getRequest().getRequestURI());
        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponse> customException(CustomException ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST);
        response.setErrorCode("BAD_REQUEST");
        response.setTitle(ex.getTitle());
        response.setMessage(ex.getMessage());
        response.setUri(((ServletWebRequest) request).getRequest().getRequestURI());
        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> unauthorizedException(UnauthorizedException ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(HttpStatus.UNAUTHORIZED);
        response.setErrorCode("UNAUTHORIZED");
        response.setTitle(ex.getTitle());
        response.setMessage(ex.getMessage());
        response.setUri(((ServletWebRequest) request).getRequest().getRequestURI());
        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.UNAUTHORIZED);
    }


}
