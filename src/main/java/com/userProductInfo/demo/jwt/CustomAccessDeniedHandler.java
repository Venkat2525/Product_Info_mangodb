package com.userProductInfo.demo.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        Map<String, String> error = new HashMap<>();
        error.put("status", "403 Forbidden");
        error.put("message", "Access Denied! You don't have permission to perform this action.");
        error.put("requiredRole", "ROLE_ADMIN");
        error.put("yourRole", "ROLE_USER");
        error.put("path", request.getRequestURI());

        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }
}