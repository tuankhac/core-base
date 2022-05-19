package com.vmo.core.common.ratelimit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiRateLimit {
    boolean isEnable() default false;

    RateLimitIdentityMode identity() default RateLimitIdentityMode.IP;

    String[] whitelist() default "";

    int maxLimit() default 50;

    /**
     * in seconds
     */
    long duration() default 60;

    boolean requireCookie() default false;
}
