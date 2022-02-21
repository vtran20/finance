/*
 * Copyright (c) 2021.
 */

package com.easysoft.finance.web.filter;

import com.easysoft.finance.configuration.MyUserDetailService;
import com.easysoft.finance.configuration.exception.base.ExceptionResponse;
import com.easysoft.finance.configuration.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private MyUserDetailService myUserDetailService;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String username = null;
        String jwt = null;
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                username = jwtUtil.extractUsername(jwt);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.myUserDetailService.loadUserByUsername(username);
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                } else {
                    throw new RuntimeException("Your account was expired!");
                }
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (RuntimeException e) {
            // ErrorResponse is a public return object that you define yourself
            ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.UNAUTHORIZED);
            exceptionResponse.setStatus(HttpStatus.UNAUTHORIZED);
            exceptionResponse.setTitle("Unauthorized Access");
            exceptionResponse.setUri(httpServletRequest.getRequestURI());
            exceptionResponse.setMessage("Your login was expired. Please login again.");
            exceptionResponse.setTimestamp(LocalDateTime.now());

            String serialized = new ObjectMapper().writeValueAsString(exceptionResponse);
            httpServletResponse.setHeader("Content-Type", "application/json");
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.getOutputStream().write(serialized.getBytes());

        }

    }
}
