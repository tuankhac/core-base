package com.vmo.core.configs.security.authen.widget;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmo.core.configs.security.authen.ajax.TokenAuthenticationFailureHandler;
import com.vmo.core.configs.security.authen.extractor.TokenExtractor;
import com.vmo.core.configs.security.exception.AuthenticationFailException;
import com.vmo.core.configs.security.exception.NoAuthorizationException;
import org.springframework.http.MediaType;
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

public class WidgetAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private final TokenExtractor tokenExtractor;
    private final TokenAuthenticationFailureHandler failureHandler;

    public WidgetAuthenticationProcessingFilter(
            RequestMatcher requestMatcher,
            ObjectMapper objectMapper,
            TokenExtractor tokenExtractor
    ) {
        super(requestMatcher);
        this.failureHandler = new TokenAuthenticationFailureHandler(objectMapper);
        this.tokenExtractor = tokenExtractor;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse
    ) throws AuthenticationException, IOException, ServletException {
        Authentication authentication;
        try {
            String token = tokenExtractor.extract(httpServletRequest);
            WidgetAuthenticationToken widgetAuthenticationToken = new WidgetAuthenticationToken(token);
            authentication = getAuthenticationManager().authenticate(widgetAuthenticationToken);
        } catch (NoAuthorizationException e) {
            //nothing to do for widget auth
            throw e;
        } catch (Exception e) {
            throw new AuthenticationFailException(e.getMessage(), e);
        }
        return authentication;
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response,
            FilterChain chain, Authentication authResult
    ) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);

    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed
    ) throws IOException, ServletException {
        SecurityContextHolder.clearContext();

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        failureHandler.onAuthenticationFailure(request, response, failed);
    }
}
