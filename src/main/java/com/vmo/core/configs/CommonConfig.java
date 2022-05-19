package com.vmo.core.configs;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.vmo.core.Constants;
import com.vmo.core.common.CommonConstants;
import com.vmo.core.common.json.TrimWhiteSpaceDeserializer;
import com.vmo.core.common.utils.CommonUtils;
import com.vmo.core.configs.env.Environments;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration("commonConfig")
@ConfigurationProperties(prefix = CommonConstants.CONFIG_PREFIX + ".common")
//@EnableConfigurationProperties(CommonConfig.class)
@ComponentScan({
        Constants.Links.COMMON_PACKAGE,
        Constants.Links.FILTERS_PACKAGE,
        Constants.Links.MODULE_PACKAGE,
//        Constants.Links.SERVICES_PACKAGE,
        Constants.Links.MANAGERS_PACKAGE,
        Constants.Links.REPOSITORIES_PACKAGE,
//        Constants.Links.MODELS_PACKAGE,
        Constants.Links.LOGGING_PACKAGE,
})
@Data
public class CommonConfig {
    private String service = "Unknown";
    private Environments env = Environments.LOCAL;
    private Boolean logApiError = false;

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        CommonUtils.updateTimeConvertObjectMapper(objectMapper);

        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, new TrimWhiteSpaceDeserializer());
        objectMapper.registerModule(module);

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        return objectMapper;
    }
//    @Bean
//    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
//        return new RequestMappingHandlerMapping();
//    }

}
