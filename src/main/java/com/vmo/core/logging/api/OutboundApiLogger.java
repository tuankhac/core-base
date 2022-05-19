package com.vmo.core.logging.api;

import com.vmo.core.configs.CommonConfig;
import com.vmo.core.logging.BaseLogger;
import com.vmo.core.logging.repositories.LogApiConfigRepository;
import com.vmo.core.modules.models.database.entities.shared.log.LogApiConfig;
import com.vmo.core.modules.models.database.entities.shared.log.LogOutboundApi;
import com.vmo.core.modules.models.database.types.log.ApiCallSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class OutboundApiLogger extends BaseLogger<LogOutboundApi> {
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private LogApiConfigRepository logApiConfigRepository;

    private volatile List<LogApiConfig> activeConfig = new ArrayList<>(); //cached configs

    public OutboundApiLogger(ApplicationContext applicationContext, @Autowired(required = false) HikariDataSource hikariDataSource) {
        super(applicationContext, hikariDataSource);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        reloadConfig();
    }

    @Override
    public void reloadConfig() {
        activeConfig = logApiConfigRepository.findAllActive(ApiCallSource.OUTBOUND);
    }

    @Async
    public void saveLogAsync(
            String url, HttpMethod httpMethod, String requestQuery, String requestBody,
            int httpStatus, String responseBody
    ) {
        LogOutboundApi log = new LogOutboundApi();
        log.setService(commonConfig.getService());
        log.setUrl(url);
        log.setHttpMethod(httpMethod.name());
        log.setRequestQuery(requestQuery);
        log.setRequestBody(requestBody);
        log.setResponseHttpStatusCode(httpStatus);
        log.setResponseBody(responseBody);

        addLog(log);
    }

    public boolean shouldLog(HttpRequest request, ClientHttpResponse response) {
        return determineConfig(request, response) != null;
    }

    private LogApiConfig determineConfig(HttpRequest request, ClientHttpResponse response) {
//        System.out.println("log api " + request.getServletPath() + " @@@ " + request.getContextPath() + " *** " + request.getRequestURI() + " ### " + request.getRequestURL());
        for (LogApiConfig config : activeConfig) {
            if (request != null && config.getHttpMethod().equals(request.getMethod())
                    && (request.getURI().getPath().equals(config.getUrl())
                    || Pattern.matches(config.getUrl(), request.getURI().toString()))
            ) {
                return config;
            }
        }
        return null;
    }
}
