package com.vmo.core.configs.security.authen.secret;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmo.core.common.CommonResponseMessages;
import com.vmo.core.configs.CommonDataSourceConfig;
import com.vmo.core.configs.security.authen.ajax.KeyAPIAuthenticationFailureHandler;
import com.vmo.core.configs.security.authen.extractor.TokenExtractor;
import com.vmo.core.configs.security.exception.AuthenticationFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class KeyAPIAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private static final Logger LOG = LoggerFactory.getLogger(KeyAPIAuthenticationProcessingFilter.class);
    private final KeyAPIAuthenticationFailureHandler failureHandler;
    private final TokenExtractor tokenExtractor;

    public KeyAPIAuthenticationProcessingFilter(
            RequestMatcher requestMatcher,
            TokenExtractor tokenExtractor,
            ObjectMapper objectMapper
    ) {
        super(requestMatcher);
        this.tokenExtractor = tokenExtractor;
        this.failureHandler = new KeyAPIAuthenticationFailureHandler(objectMapper);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            String secretKey = tokenExtractor.extract(request);
            if (secretKey == null || !(CommonDataSourceConfig.getSecretKey()).equals(secretKey)) {
                throw new BadCredentialsException(CommonResponseMessages.INVALID_API_KEY);
            }
            return getAuthenticationManager().authenticate(new KeyAPIAuthentication(secretKey));
        } catch (Exception e) {
            throw new AuthenticationFailException(e.getMessage(), e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        failureHandler.onAuthenticationFailure(request, response, failed);
    }

}
