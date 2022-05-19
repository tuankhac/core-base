package com.vmo.core.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmo.core.common.CommonResponseMessages;
import com.vmo.core.modules.models.response.ErrorCode;
import com.vmo.core.modules.models.response.ErrorResponse;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import org.joda.time.LocalDateTime;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * determinate system resource to process the incoming request is enough, if no reject the request
 */
@Component
@WebFilter
@Order(Ordered.LOWEST_PRECEDENCE)
public class PerformanceAwareFilter implements Filter {
    private final ObjectMapper objectMapper;
    private final HikariDataSource hikariDataSource;
    private HikariPool hikariPool;

    @Autowired
    public PerformanceAwareFilter(ObjectMapper objectMapper, HikariDataSource hikariDataSource) {
        this.objectMapper = objectMapper;
        this.hikariDataSource = hikariDataSource;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (hikariDataSource != null) {
            hikariPool = (HikariPool) new DirectFieldAccessor(hikariDataSource).getPropertyValue("pool");
        }
    }

    @Override
    public void doFilter(
            ServletRequest servletRequest, ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {
        if (hikariPool != null) {
            if (hikariPool.getIdleConnections() / (float) hikariPool.getTotalConnections() <= 0.05
                    && hikariPool.getIdleConnections() <= 10
            ) {
                ErrorResponse error = new ErrorResponse(CommonResponseMessages.SERVICE_TEMPORARY_UNAVAILABLE, ErrorCode.SERVICE_TEMPORARY_UNAVAILABLE);
                error.setTime(LocalDateTime.now());
                if (servletRequest instanceof HttpServletRequest) {
                    HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
                    error.setHttpMethod(httpRequest.getMethod());
                    error.setPath(httpRequest.getServletPath());
                }

                if (servletResponse instanceof HttpServletResponse) {
                    ((HttpServletResponse) servletResponse).setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
                }
                objectMapper.writeValue(servletResponse.getWriter(), error);

                return;
            }
        }

        //TODO check system cpu...

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
