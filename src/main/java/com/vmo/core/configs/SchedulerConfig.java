package com.vmo.core.configs;

import com.vmo.core.Constants;
import com.vmo.core.common.CommonConstants;
import com.vmo.core.schedulers.handler.JobFailHandler;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@ConfigurationProperties(prefix = CommonConstants.CONFIG_PREFIX + ".scheduler")
@ComponentScan({Constants.Links.SCHEDULER_PACKAGE})
@Data
public class SchedulerConfig {

    private static ThreadPoolTaskScheduler jobThreadPool;
    private Boolean enable;
    private Boolean cleanUnused;
    private Integer firstRunDelay;
    private Integer threadsSize = 5;
    @Autowired
    private JobFailHandler jobFailHandler;

    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        if (jobThreadPool == null) {
            jobThreadPool = new ThreadPoolTaskScheduler();
            jobThreadPool.setPoolSize(threadsSize);
            jobThreadPool.setThreadNamePrefix("JobsThreadPool");
            jobThreadPool.setErrorHandler(jobFailHandler);
            jobThreadPool.initialize();
        }
        return jobThreadPool;
    }
}
