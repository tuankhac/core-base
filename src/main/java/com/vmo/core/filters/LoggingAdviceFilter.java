package com.vmo.core.filters;

import com.vmo.core.logging.api.InboundApiLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@WebFilter
@Order(Ordered.LOWEST_PRECEDENCE)
public class LoggingAdviceFilter implements Filter {
    @Autowired
    private InboundApiLogger inboundApiLogger;

    @Override
    public void doFilter(
            ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain
    ) throws IOException, ServletException {
        if (servletResponse instanceof HttpServletResponse
                && servletRequest instanceof HttpServletRequest
        ) {
            boolean finishedChain = false;
            ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(
                    (HttpServletRequest) servletRequest);
            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(
                    (HttpServletResponse) servletResponse);
            try {
//            new HttpServletResponseWrapper((HttpServletResponse) servletResponse) {
//                @Override
//                public ServletOutputStream getOutputStream() throws IOException {
//                    return new DelegatingServletOutputStream(
//                            new TeeOutputStream(super.getOutputStream(), loggingOutputStream())
//                    );
//                }
//            };


//            filterChain.doFilter(servletRequest, servletResponse);
                filterChain.doFilter(requestWrapper, responseWrapper);
                finishedChain = true;

                if (inboundApiLogger.shouldLog(requestWrapper, responseWrapper)) {
                    String requestBody = new String(requestWrapper.getContentAsByteArray());
                    String responseBody = new String(responseWrapper.getContentAsByteArray());

                    inboundApiLogger.saveLogAsync(
                            requestWrapper.getRequestURI(), requestWrapper.getMethod(), requestWrapper.getQueryString(), requestBody,
                            responseWrapper.getStatus(), responseBody
                    );
                }

                responseWrapper.copyBodyToResponse();
            } catch (Exception e) {
                e.printStackTrace();
                if (!finishedChain) {
                    filterChain.doFilter(requestWrapper, responseWrapper);
                }
                responseWrapper.copyBodyToResponse();
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
