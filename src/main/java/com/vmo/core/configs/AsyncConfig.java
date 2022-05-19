package com.vmo.core.configs;

import com.vmo.core.logging.asynctask.AsyncTaskLogger;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync(proxyTargetClass = true)
public class AsyncConfig implements AsyncConfigurer {
    @Autowired
    @Lazy
    private AsyncTaskLogger asyncTaskLogger;

//    @Override
//    public Executor getAsyncExecutor() {
//        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(7);
//        executor.setMaxPoolSize(42);
//        executor.setQueueCapacity(11);
//        executor.setThreadNamePrefix("@AsyncExecutor-");
//        executor.initialize();
//        return new AsyncTaskFactory(executor, asyncTaskLogger);
//    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return asyncTaskLogger;
    }
}
