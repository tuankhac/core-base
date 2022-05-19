package com.vmo.core.configs.security.authen.ajax;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmo.core.modules.models.response.ErrorCode;
import com.vmo.core.modules.models.response.ErrorResponse;
import org.joda.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class KeyAPIAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper objectMapper;

    public KeyAPIAuthenticationFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        ErrorResponse error = new ErrorResponse(e.getMessage(), ErrorCode.NO_PERMISSION);
        error.setTime(LocalDateTime.now());
        error.setPath(httpServletRequest.getServletPath());
        objectMapper.writeValue(httpServletResponse.getWriter(), error);
    }
}
