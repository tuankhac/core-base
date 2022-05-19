package com.vmo.core.configs.security.authen.ajax;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmo.core.modules.models.response.ErrorCode;
import com.vmo.core.modules.models.response.ErrorResponse;
import org.joda.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationFailureHandler implements AuthenticationFailureHandler {
    public final ObjectMapper objectMapper;

    public TokenAuthenticationFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException e
    ) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ErrorResponse error = new ErrorResponse(e.getMessage(), ErrorCode.UNAUTHORIZED);
        error.setTime(LocalDateTime.now());
        error.setPath(request.getServletPath());
        objectMapper.writeValue(response.getWriter(), error);
    }
}
