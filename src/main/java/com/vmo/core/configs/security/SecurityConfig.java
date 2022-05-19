package com.vmo.core.configs.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmo.core.Constants;
import com.vmo.core.common.CommonConstants;
import com.vmo.core.configs.security.authen.extractor.HeaderTokenExtractor;
import com.vmo.core.configs.security.authen.extractor.SecretKeyExtractor;
import com.vmo.core.configs.security.authen.extractor.WidgetTokenExtractor;
import com.vmo.core.configs.security.authen.jwt.JwtAuthenticationProcessingFilter;
import com.vmo.core.configs.security.authen.jwt.JwtAuthenticationProvider;
import com.vmo.core.configs.security.authen.secret.KeyAPIAuthenticationProcessingFilter;
import com.vmo.core.configs.security.authen.secret.KeyAPIAuthenticationProvider;
import com.vmo.core.configs.security.authen.widget.WidgetAuthenticationProcessingFilter;
import com.vmo.core.configs.security.authen.widget.WidgetAuthenticationProvider;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.Filter;

@EnableWebSecurity
@Configuration
@ConfigurationProperties(prefix = CommonConstants.CONFIG_PREFIX + ".security")
//@EnableConfigurationProperties(SecurityConfig.class)
@ComponentScan(Constants.Links.SECURITY_PACKAGE)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Getter
    @Setter
    private Boolean requireAuthenticationByDefault = true;
    @Getter
    @Setter
    private String secretKey;
    @Getter
    @Setter
    private String tokenSecret;
    @Getter
    @Setter
    private String matchingApiUser;
    @Getter
    @Setter
    private String matchingApiPrivate;
    @Getter
    @Setter
    private String matchingApiWidget;
    @Getter
    @Setter
    private String countdownApiKey;
    @Getter
    @Setter
    private String attentiveToken;

    @Autowired
    private ObjectMapper objectMapper;
    //    @Autowired
//    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;
    @Autowired
    private KeyAPIAuthenticationProvider keyAPIAuthenticationProvider;
    @Autowired
    private WidgetAuthenticationProvider widgetAuthenticationProvider;
    @Autowired
    private HeaderTokenExtractor headerTokenExtractor;
    @Autowired
    private SecretKeyExtractor secretKeyExtractor;
    @Autowired
    private WidgetTokenExtractor widgetTokenExtractor;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(jwtAuthenticationProvider);
        auth.authenticationProvider(keyAPIAuthenticationProvider);
        auth.authenticationProvider(widgetAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        Filter jwtAuth = buildJwtTokenAuthenticationProcessingFilter(getMatchingApiUser());
        if (jwtAuth != null) {
            http.addFilterBefore(jwtAuth, UsernamePasswordAuthenticationFilter.class);
        }

        Filter keyAPIAuth = buildKeyAPIAuthenticationProcessingFilter(getMatchingApiPrivate());
        if (keyAPIAuth != null) {
            http.addFilterBefore(keyAPIAuth, UsernamePasswordAuthenticationFilter.class);
        }

        Filter widgetAuth = buildWidgetAuthenticationProcessingFilter(getMatchingApiWidget());
        if (widgetAuth != null) {
            http.addFilterBefore(widgetAuth, UsernamePasswordAuthenticationFilter.class);
        }
    }

    private JwtAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter(
            String apiUrlPattern
    ) {
        if (StringUtils.isBlank(apiUrlPattern)) {
            return null;
        }

        RequestMatcher requestMatcher = new AntPathRequestMatcher(apiUrlPattern);
        JwtAuthenticationProcessingFilter filter = new JwtAuthenticationProcessingFilter(
                requestMatcher,
                headerTokenExtractor,
                applicationContext,
                this,
                objectMapper
        );
        try {
            filter.setAuthenticationManager(authenticationManagerBean());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filter;
    }

    private KeyAPIAuthenticationProcessingFilter buildKeyAPIAuthenticationProcessingFilter(
            String apiUrlPattern
    ) {
        if (StringUtils.isBlank(apiUrlPattern)) {
            return null;
        }

        RequestMatcher requestMatcher = new AntPathRequestMatcher(apiUrlPattern);
        KeyAPIAuthenticationProcessingFilter filter = new KeyAPIAuthenticationProcessingFilter(
                requestMatcher,
                secretKeyExtractor,
                objectMapper
        );
        try {
            filter.setAuthenticationManager(authenticationManagerBean());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filter;
    }

    private WidgetAuthenticationProcessingFilter buildWidgetAuthenticationProcessingFilter(
            String apiUrlPattern
    ) {
        if (StringUtils.isBlank(apiUrlPattern)) {
            return null;
        }

        RequestMatcher requestMatcher = new AntPathRequestMatcher(apiUrlPattern);
        WidgetAuthenticationProcessingFilter filter = new WidgetAuthenticationProcessingFilter(
                requestMatcher,
                objectMapper,
                widgetTokenExtractor
        );
        try {
            filter.setAuthenticationManager(authenticationManagerBean());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filter;
    }

//    @Bean
//    @ConditionalOnMissingBean(JwtAuthenticationProvider.class)
//    public JwtAuthenticationProvider jwtAuthenticationProvider() {
//        return new JwtAuthenticationProvider();
//    }
//
//    @Bean
//    @ConditionalOnMissingBean(JwtTokenFactory.class)
//    public JwtTokenFactory jwtTokenFactory() {
//        return new JwtTokenFactory();
//    }
//
//    @Bean
//    @ConditionalOnMissingBean(KeyAPIAuthenticationProvider.class)
//    public KeyAPIAuthenticationProvider keyAPIAuthenticationProvider() {
//        return new KeyAPIAuthenticationProvider();
//    }
//
//    @Bean
//    @ConditionalOnMissingBean(HeaderTokenExtractor.class)
//    public HeaderTokenExtractor headerTokenExtractor() {
//        return new HeaderTokenExtractor();
//    }
//
//    @Bean
//    @ConditionalOnMissingBean(SecretKeyExtractor.class)
//    public SecretKeyExtractor secretKeyExtractor() {
//        return new SecretKeyExtractor();
//    }
}
