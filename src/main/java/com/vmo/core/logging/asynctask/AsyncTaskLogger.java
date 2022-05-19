package com.vmo.core.logging.asynctask;

import com.vmo.core.configs.CommonConfig;
import com.vmo.core.logging.BaseLogger;
import com.vmo.core.logging.repositories.LogAsyncExcludeRepository;
import com.vmo.core.modules.models.database.entities.shared.log.LogAsyncExclude;
import com.vmo.core.modules.models.database.entities.shared.log.LogAsyncTaskError;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

@Component
public class AsyncTaskLogger extends BaseLogger<LogAsyncTaskError> implements AsyncUncaughtExceptionHandler {
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private LogAsyncExcludeRepository logAsyncExcludeRepository;

    private volatile List<LogAsyncExclude> excludedLogs; //cached configs

    public AsyncTaskLogger(ApplicationContext applicationContext, @Autowired(required = false) HikariDataSource hikariDataSource) {
        super(applicationContext, hikariDataSource);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        reloadConfig();
    }

    @Override
    public void reloadConfig() {
        excludedLogs = logAsyncExcludeRepository.findAllExcludedLogs();
    }

    @Override
    public void handleUncaughtException(
            Throwable throwable, Method method, Object... obj
    ) {
//        System.out.println("Exception message - " + throwable.getMessage());
//        System.out.println("Method name - " + method.getName());
//        for (Object param : obj) {
//            System.out.println("Parameter value - " + param);
//        }


        LogAsyncTaskError log = new LogAsyncTaskError();
        log.setService(commonConfig.getService());
        log.setMethod(method.getDeclaringClass().getName() + "." + method.getName());
        log.setExceptionName(throwable.getClass().getSimpleName());

        boolean isExcluded = false;
        if (CollectionUtils.isNotEmpty(excludedLogs)) {
            for (LogAsyncExclude exclude : excludedLogs) {
                if (exclude.getMethod() != null) {
                    if (exclude.getMethod().equals(log.getMethod())) {
                        isExcluded = true;
                    }
                }

                if (exclude.getExceptionName() != null) {
                    if (exclude.getExceptionName().equals(log.getExceptionName())) {
                        isExcluded = true && isExcluded;
                    } else {
                        isExcluded = false;
                    }
                }

                if (isExcluded) {
                    break;
                }
            }
        }

        if (!isExcluded) {
            log.setStacktrace(ExceptionUtils.getStackTrace(throwable));

            addLog(log);
        }
    }
}
