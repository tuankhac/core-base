package com.vmo.core.filters;

import com.vmo.core.configs.http.CacheServletRequestWrapper;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebFilter implements Filter {
    private ObjectMapper objectMapper;
    private String env;

    @Autowired
    public WebFilter(@Qualifier(value = "objectMapper") ObjectMapper objectMapper, Environment environment) {
        this.objectMapper = objectMapper;
        String[] envs = environment.getActiveProfiles();
        this.env = envs.length == 0 ? null : envs[0];
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            try {

                filterChain.doFilter(
                        new CacheServletRequestWrapper((HttpServletRequest) servletRequest, objectMapper, env),
                        servletResponse
                );
            } catch (HttpMessageNotReadableException | JsonMappingException | NumberFormatException e) {
                filterChain.doFilter(servletRequest, servletResponse);
            } catch (Exception e) {
                filterChain.doFilter(servletRequest, servletResponse);
            }

        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }

    @Override
    public void destroy() {
    }
}
