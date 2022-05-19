package com.vmo.core.configs;

import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.vmo.core.common.utils.CommonUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.List;
import java.util.Locale;

@Configuration
@ControllerAdvice
public class WebConfig implements WebBindingInitializer, WebMvcConfigurer {
    private final ObjectMapper objectMapper;

//    @Autowired
//    private RateLimitHandler rateLimitHandler;

    @Autowired
    public WebConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @InitBinder
    @Override
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringTrimmer = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, stringTrimmer);
    }

//    @Bean
//    @ConditionalOnMissingBean(
//            value = {MappingJackson2HttpMessageConverter.class},
//            ignoredType = {"org.springframework.hateoas.server.mvc.TypeConstrainedMappingJackson2HttpMessageConverter", "org.springframework.data.rest.webmvc.alps.AlpsJsonHttpMessageConverter"}
//    )
//    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
//        return new MappingJackson2HttpMessageConverter(objectMapper);
//    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jsonConverter = null;
        MappingJackson2XmlHttpMessageConverter xmlConverter = null;
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                jsonConverter = ((MappingJackson2HttpMessageConverter) converter);
                jsonConverter.setObjectMapper(objectMapper);
            } else if (converter instanceof MappingJackson2XmlHttpMessageConverter) {
                xmlConverter = (MappingJackson2XmlHttpMessageConverter) converter;
            }
        }

        if (ObjectUtils.allNotNull(jsonConverter, xmlConverter)) {
            int jsonOrder = converters.indexOf(jsonConverter);
            int xmlOrder = converters.indexOf(xmlConverter);

            //priority json over xml
            if (jsonOrder > xmlOrder) {
                converters.set(xmlOrder, jsonConverter);
                converters.set(jsonOrder, xmlConverter);
            }
        }
    }

    //configuring default locale
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(Locale.US);
        return localeResolver;
    }

    //configuring ResourceBundle
    @Bean("messageSource")
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/message");
        return messageSource;
    }

    @Bean("apiMessageSource")
    public ReloadableResourceBundleMessageSource apiMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/api/message");
        return messageSource;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        // Defaults to "locale" if not set
        localeChangeInterceptor.setParamName("Accept-Language");
        return localeChangeInterceptor;
    }

    @Bean
    @Primary
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public XmlMapper xmlMapper() {
        XmlFactory factory = new XmlFactory(new WstxInputFactory(), new WstxOutputFactory());
        XmlMapper xmlMapper = new XmlMapper(factory);
        CommonUtils.updateTimeConvertXmlMapperEbay(xmlMapper);
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return xmlMapper;
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(rateLimitHandler);
//    }
}
