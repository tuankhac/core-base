package com.vmo.core.configs.security.authen.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmo.core.configs.security.SecurityConfig;
import com.vmo.core.configs.security.annotation.Authorized;
import com.vmo.core.configs.security.authen.ajax.TokenAuthenticationFailureHandler;
import com.vmo.core.configs.security.authen.extractor.TokenExtractor;
import com.vmo.core.configs.security.exception.AuthenticationFailException;
import com.vmo.core.configs.security.exception.NoAuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

public class JwtAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationProcessingFilter.class);
    private final TokenExtractor tokenExtractor;
    private final AuthenticationFailureHandler failureHandler;
    private final ApplicationContext applicationContext;
    private final SecurityConfig securityConfig;

    public JwtAuthenticationProcessingFilter(
            RequestMatcher matcher,
            TokenExtractor tokenExtractor,
            ApplicationContext applicationContext,
            SecurityConfig securityConfig,
            ObjectMapper objectMapper
    ) {
        super(matcher);
        this.tokenExtractor = tokenExtractor;
        this.applicationContext = applicationContext;
        this.failureHandler = new TokenAuthenticationFailureHandler(objectMapper);
        this.securityConfig = securityConfig;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response
    ) throws AuthenticationException, IOException, ServletException {
        Authentication authentication;
        try {
            String token = tokenExtractor.extract(request);
            authentication = getAuthenticationManager().authenticate(new JwtAuthenticationToken(token));
        } catch (NoAuthorizationException e) {
            try {
                for (HandlerMapping value : applicationContext.getBeansOfType(HandlerMapping.class).values()) {
                    HandlerExecutionChain handler = value.getHandler(request);
                    if (handler != null && handler.getHandler() instanceof HandlerMethod) {
                        HandlerMethod handlerMethod = (HandlerMethod) handler.getHandler();
                        Method method = handlerMethod.getMethod();
                        Authorized authorized = method.getAnnotation(Authorized.class);
                        if (authorized != null && !authorized.isRequired()) {
                            return new GuestAuthentication();
                        }
                    }
                }
//                HandlerExecutionChain handler = requestMappingHandlerMapping.getHandler(request);
//                if (handler != null && handler.getHandler() instanceof HandlerMethod) {
//                    HandlerMethod handlerMethod = (HandlerMethod)handler.getHandler();
//                    Method method = handlerMethod.getMethod();
//                    Authorized authorized = method.getAnnotation(Authorized.class);
//                    if (authorized != null && !authorized.isRequired()) {
//                        return new GuestAuthentication();
//                    }
//                }

                if (securityConfig.getRequireAuthenticationByDefault() != null
                        && !securityConfig.getRequireAuthenticationByDefault()
                ) {
                    return new GuestAuthentication();
                }
            } catch (Exception ex2) {
            }
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
        if (!response.isCommitted()) {
            chain.doFilter(request, response);
        }
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
