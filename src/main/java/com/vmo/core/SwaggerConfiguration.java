package com.vmo.core;

import com.vmo.core.common.CommonConstants;
import com.vmo.core.common.converter.StringToLocalDateConverter;
import com.vmo.core.common.converter.StringToLocalDateTimeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private ConverterRegistry converterRegistry;
    @Autowired
    private AppConfig appConfig;

    @PostConstruct
    public void init() {
        converterRegistry.addConverter(new StringToLocalDateTimeConverter());
        converterRegistry.addConverter(new StringToLocalDateConverter());
    }

    @Bean
    public Docket api(ServletContext servletContext) {
        String contextPath = appConfig.getEndpoint() != null ? appConfig.getEndpoint().getBaseCoreApiUrl() : "";
        String basePath = appConfig.getEndpoint() != null ? appConfig.getEndpoint().getCoreApiPrefix() : null;
        if (contextPath.contains("localhost")) {
            contextPath = null;
        }
        List<Parameter> parameterBuilders = new ArrayList<>();
        parameterBuilders.add(
                new ParameterBuilder()
                        .name(CommonConstants.HEADER_AUTH_NAME)
                        .description("User token")
                        .modelRef(new ModelRef("string"))
                        .parameterType("header")
                        .required(false)
                        .build());
        parameterBuilders.add(
                new ParameterBuilder()
                        .name(CommonConstants.HEADER_SECRET_KEY)
                        .description("Secret key")
                        .modelRef(new ModelRef("string"))
                        .parameterType("header")
                        .required(false)
                        .build());
        if (contextPath == null) {
            return new Docket(DocumentationType.SWAGGER_2)
                    .select()
                    .apis(requestHandler -> requestHandler.declaringClass().getName().matches(Constants.CONTROLLERS_SWAGGER_PACKAGE))
                    .paths(PathSelectors.any())
                    .build()
                    .pathMapping("/")
                    .globalOperationParameters(parameterBuilders)
                    .ignoredParameterTypes(Pageable.class);
        } else {
            return new Docket(DocumentationType.SWAGGER_2)
                    .host(contextPath)
                    .pathProvider(new RelativePathProvider(servletContext) {
                        @Override
                        public String getApplicationBasePath() {
                            return (basePath != null ? basePath : super.getApplicationBasePath());
                        }
                    })
                    .select()
                    .apis(requestHandler -> requestHandler.declaringClass().getName().matches(Constants.CONTROLLERS_SWAGGER_PACKAGE))
                    .paths(PathSelectors.any())
                    .build()
                    .pathMapping("/")
                    .globalOperationParameters(parameterBuilders)
                    .ignoredParameterTypes(Pageable.class);
        }

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //registry.addViewController("/").setViewName("forward:index.html");
        registry.addRedirectViewController("/configuration/ui", "/swagger-resources/configuration/ui");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, CommonConstants.PAGE_SIZE_DEFAULT));
        resolver.setOneIndexedParameters(true);
        argumentResolvers.add(resolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH");
    }

}