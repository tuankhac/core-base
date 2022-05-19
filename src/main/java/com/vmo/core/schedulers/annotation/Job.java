package com.vmo.core.schedulers.annotation;

import com.vmo.core.modules.models.database.types.job.JobTimeType;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Job {
    /**
     * Should this job be enabled by default? Can be overridden by configuration from database
     */
    boolean preEnable() default false;

    /**
     * Ignore setting from any source, always disable the job
     */
    boolean forceDisable() default false;

    /**
     * Override setting from database, enable the job. Useful for local debugging
     */
    boolean overrideEnable() default false;

    JobTimeType defaultTimeType() default JobTimeType.FIX_DELAY;

    long defaultTimeSeconds() default 3600;

    String defaultCron() default "";

    boolean defaultLogFail() default true;
}
