package com.vmo.core.logging.api;

import com.vmo.core.configs.CommonConfig;
import com.vmo.core.logging.BaseLogger;
import com.vmo.core.logging.repositories.LogApiConfigRepository;
import com.vmo.core.modules.models.database.entities.shared.log.LogApiConfig;
import com.vmo.core.modules.models.database.entities.shared.log.LogInboundApi;
import com.vmo.core.modules.models.database.types.log.ApiCallSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class InboundApiLogger extends BaseLogger<LogInboundApi> {
    //    private HikariPool hikariPool;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private LogApiConfigRepository logApiConfigRepository;

    private volatile List<LogApiConfig> activeConfig = new ArrayList<>(); //cached configs

    public InboundApiLogger(ApplicationContext applicationContext, @Autowired(required = false) HikariDataSource hikariDataSource) {
        super(applicationContext, hikariDataSource);
    }

//    public InboundApiLogger(HikariDataSource hikariDataSource) {
//        hikariPool = (HikariPool) new DirectFieldAccessor(hikariDataSource).getPropertyValue("pool");
//    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        reloadConfig();
    }

    @Override
    public void reloadConfig() {
        activeConfig = logApiConfigRepository.findAllActive(ApiCallSource.INBOUND);
    }

    //extends HandlerInterceptorAdapter {
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        super.postHandle(request, response, handler, modelAndView);
//        System.out.println("post handle " + LocalDateTime.now());
//    }

//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        super.afterCompletion(request, response, handler, ex);
//        System.out.println("after complete " + LocalDateTime.now());
//    }

    @Async
    public void saveLogAsync(
            String path, String httpMethod, String requestQuery, String requestBody,
            int httpStatus, String responseBody
    ) {
//        saveLogSync(response);

        LogInboundApi log = new LogInboundApi();
        log.setService(commonConfig.getService());
        log.setPath(path);
        log.setHttpMethod(HttpMethod.resolve(httpMethod));
        log.setRequestQuery(requestQuery);
        log.setRequestBody(requestBody);
        log.setResponseHttpStatusCode(httpStatus);
        log.setResponseBody(responseBody);

        addLog(log);
    }

//    private synchronized void saveLogSync(HttpServletResponse response) {
//        ;
//    }

    public boolean shouldLog(HttpServletRequest request, HttpServletResponse response) {
        return determineConfig(request, response) != null;
    }

    private LogApiConfig determineConfig(HttpServletRequest request, HttpServletResponse response) {
//        System.out.println("log api " + request.getServletPath() + " @@@ " + request.getContextPath() + " *** " + request.getRequestURI() + " ### " + request.getRequestURL());
        for (LogApiConfig config : activeConfig) {
            if (request.getMethod().equalsIgnoreCase(config.getHttpMethod().name())
                    && (request.getRequestURI().equals(config.getUrl())
                    || Pattern.matches(config.getUrl(), request.getRequestURI()))
            ) {
                return config;
            }
        }
        return null;
    }
}
