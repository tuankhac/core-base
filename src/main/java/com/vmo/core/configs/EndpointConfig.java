package com.vmo.core.configs;

import com.vmo.core.common.CommonConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = CommonConstants.CONFIG_PREFIX + ".common.endpoints")
@Data
public class EndpointConfig {
    private String currentServiceBaseApiUrl;
    private String baseApiUrl;
    private String baseValueGuideApiUrl;
    private String coreApiPrefix;
    private String authApiPrefix;
    private String billingApiPrefix;
    private String notifyApiPrefix;
    private String subscriptionApiPrefix;
    private String shipmentApiPrefix;
    private String valueGuideApiPrefix;
    private String supportApiPrefix;
    private String tradeCreditApiPrefix;
    private String baseAdminPortalUrl;
    private String salesforceApiUrl;
    private String baseWebappUrl;
}
