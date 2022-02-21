/*
 * Copyright (c) 2021.
 */

package com.easysoft.finance.domain.auth;

public class AuthenticationResponse {

    private final String username;
    private final String token;

    public AuthenticationResponse(String userName, String token) {
        this.username = userName;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }
}
