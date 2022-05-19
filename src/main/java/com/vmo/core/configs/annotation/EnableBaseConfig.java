package com.vmo.core.configs.annotation;

import com.vmo.core.configs.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({
        CommonConfig.class, EndpointConfig.class, WebConfig.class,
        AsyncConfig.class, SlackWebhookConfig.class,
        CommonDataSourceConfig.class, SchedulerConfig.class
})
@Configuration
public @interface EnableBaseConfig {
}
