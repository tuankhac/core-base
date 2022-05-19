package com.vmo.core.configs.security.annotation;

import com.vmo.core.configs.security.SecurityConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(SecurityConfig.class)
//@EnableConfigurationProperties(SecurityConfig.class) //can use either import or enable config
@Configuration
public @interface EnableSecurityModule {
}
