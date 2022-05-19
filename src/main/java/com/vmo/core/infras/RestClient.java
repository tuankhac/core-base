package com.vmo.core.infras;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmo.core.common.CommonConstants;
import com.vmo.core.common.utils.CommonUtils;
import com.vmo.core.configs.CommonConfig;
import com.vmo.core.logging.api.OutboundApiLogger;
import com.vmo.core.logging.api.LoggerClientHttpRequestInterceptor;
import com.vmo.core.modules.models.response.ListObjResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

public abstract class RestClient {
    private final static Logger LOG = LoggerFactory.getLogger(RestClient.class);
    private String baseUrl;
    private RestTemplate restTemplate;

    public RestClient(String baseUrl, OutboundApiLogger outboundApiLogger, CommonConfig commonConfig) {
        this.baseUrl = baseUrl;
        init(outboundApiLogger);

        restTemplate.getMessageConverters().add(0, CommonUtils.converterJack());
    }

    @Deprecated
    public RestClient(String baseUrl, OutboundApiLogger outboundApiLogger) {
        this.baseUrl = baseUrl;
        init(outboundApiLogger);

        restTemplate.getMessageConverters().add(0, CommonUtils.converterJack());
    }

    @Deprecated
    public RestClient(String baseUrl, OutboundApiLogger outboundApiLogger, MappingJackson2HttpMessageConverter converter) {
        this.baseUrl = baseUrl;
        init(outboundApiLogger);

        restTemplate.getMessageConverters().add(0, converter);
    }

    public RestClient objectMapper(ObjectMapper objectMapper) {
        if (objectMapper != null) {
            MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
            jsonConverter.setPrettyPrint(false);
            jsonConverter.setObjectMapper(objectMapper);

            if (CollectionUtils.isNotEmpty(restTemplate.getMessageConverters())) {
                List<HttpMessageConverter> removeConverters = new ArrayList<>();
                for (HttpMessageConverter<?> messageConverter : restTemplate.getMessageConverters()) {
                    if (messageConverter instanceof MappingJackson2HttpMessageConverter) {
                        removeConverters.add(messageConverter);
                    }
                }

                if (CollectionUtils.isNotEmpty(removeConverters)) {
                    restTemplate.getMessageConverters().removeAll(removeConverters);
                }
            } else {
                restTemplate.setMessageConverters(new ArrayList<>());
            }
            restTemplate.getMessageConverters().add(0, jsonConverter);
        }
        return this;
    }

    private void init(OutboundApiLogger outboundApiLogger) {
        init(outboundApiLogger, null);
    }

    private void init(OutboundApiLogger outboundApiLogger, CommonConfig commonConfig) {
        restTemplate = new RestTemplate();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout((int) (CommonConstants.MINUTE * 5));
        requestFactory.setReadTimeout((int) (CommonConstants.MINUTE * 5));

//        ObjectMapper mapper = new ObjectMapper();
//        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
//
//        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
//        messageConverter.setPrettyPrint(false);
//        messageConverter.setObjectMapper(mapper);

//        restTemplate.getMessageConverters().add(messageConverter);


        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(requestFactory));


//        if (restTemplate.getInterceptors() == null) {
//            restTemplate.setInterceptors(new ArrayList<>());
//        }
        restTemplate.getInterceptors().add(new LoggerClientHttpRequestInterceptor(outboundApiLogger, commonConfig));
    }

    public <T> T getObject(String endPoint, Class<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<T> res = restTemplate.exchange(baseUrl + endPoint, HttpMethod.GET, entity, clazz);
        return res.getBody();
    }

    public <T> T getObject(String endPoint, ParameterizedTypeReference<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<T> res = restTemplate.exchange(baseUrl + endPoint, HttpMethod.GET, entity, clazz);
        return res.getBody();

    }

    public <T> T getObject(String endPoint, Map<String, String> mapParameter, ParameterizedTypeReference<T> clazz) {
        endPoint = convertUrlPathVariable(endPoint, mapParameter);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl + endPoint);
        for (Map.Entry<String, String> entry : mapParameter.entrySet()) {
            uriComponentsBuilder = uriComponentsBuilder.queryParam(entry.getKey(), entry.getValue());
//            URLEncoder.encode(entry.getValue(), "UTF-8")
//            entry.getValue())
        }
        String uri = uriComponentsBuilder.build().toString();
        //uriComponentsBuilder.toUriString().replace("%20", " ");
        ResponseEntity<T> res = restTemplate.exchange(uri, HttpMethod.GET, entity, clazz);
        return res.getBody();
    }

    public <T> T getObject(String endPoint, Map<String, String> mapParameter, Class<T> clazz) {
        endPoint = convertUrlPathVariable(endPoint, mapParameter);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl + endPoint);
        for (Map.Entry<String, ?> entry : mapParameter.entrySet()) {
            uriComponentsBuilder = uriComponentsBuilder.queryParam(entry.getKey(), entry.getValue());
        }
        String uri = uriComponentsBuilder.build().toString();
        //uriComponentsBuilder.toUriString().replace("%20", " ");
        ResponseEntity<T> res = restTemplate.exchange(uri, HttpMethod.GET, entity, clazz);
        return res.getBody();
    }

    public <T> T getObject(String endPoint, String headerName, String headerContent, Map<String, String> mapParameter, Class<T> clazz) {
        endPoint = convertUrlPathVariable(endPoint, mapParameter);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(headerName, headerContent);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl + endPoint);
        for (Map.Entry<String, ?> entry : mapParameter.entrySet()) {
            uriComponentsBuilder = uriComponentsBuilder.queryParam(entry.getKey(), entry.getValue());
        }
        String uri = uriComponentsBuilder.build().toString();
        //uriComponentsBuilder.toUriString().replace("%20", " ");
        ResponseEntity<T> res = restTemplate.exchange(uri, HttpMethod.GET, entity, clazz);
        return res.getBody();
    }

    public <T> T getObjectPath(String endPoint, String headerName, String headerContent, Map<String, String> mapParameter, Class<T> clazz) {
        endPoint = convertUrlPathVariable(endPoint, mapParameter);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(headerName, headerContent);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<T> res = restTemplate.exchange(baseUrl + endPoint, HttpMethod.GET, entity, clazz, mapParameter);
        return res.getBody();
    }

    public <T> List<T> getObject(String endPoint, String headerName, String headerContent, Map<String, String> mapParameter, ParameterizedTypeReference<List<T>> clazz) {
        endPoint = convertUrlPathVariable(endPoint, mapParameter);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(headerName, headerContent);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl + endPoint);
        for (Map.Entry<String, ?> entry : mapParameter.entrySet()) {
            uriComponentsBuilder = uriComponentsBuilder.queryParam(entry.getKey(), entry.getValue());
        }
        String uri = uriComponentsBuilder.build().toString();
        //uriComponentsBuilder.toUriString().replace("%20", " ");
        ResponseEntity<List<T>> res = restTemplate.exchange(uri, HttpMethod.GET, entity, clazz);
        return res.getBody();
    }

    public <T> ListObjResponse<T> getListObject(String endPoint, String headerName, String headerContent, Map<String, String> mapParameter, ParameterizedTypeReference<ListObjResponse<T>> clazz) {
        endPoint = convertUrlPathVariable(endPoint, mapParameter);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(headerName, headerContent);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl + endPoint);
        for (Map.Entry<String, ?> entry : mapParameter.entrySet()) {
            uriComponentsBuilder = uriComponentsBuilder.queryParam(entry.getKey(), entry.getValue());
        }
        String uri = uriComponentsBuilder.build().toString();
        //uriComponentsBuilder.toUriString().replace("%20", " ");
        ResponseEntity<ListObjResponse<T>> res = restTemplate.exchange(uri, HttpMethod.GET, entity, clazz);
        return res.getBody();
    }

    public <T> T getObjectNotList(String endPoint, String headerName, String headerContent, Map<String, String> mapParameter, ParameterizedTypeReference<T> clazz) {
        endPoint = convertUrlPathVariable(endPoint, mapParameter);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(headerName, headerContent);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl + endPoint);
        for (Map.Entry<String, ?> entry : mapParameter.entrySet()) {
            uriComponentsBuilder = uriComponentsBuilder.queryParam(entry.getKey(), entry.getValue());
        }
        String uri = uriComponentsBuilder.build().toString();
        //uriComponentsBuilder.toUriString().replace("%20", " ");
        ResponseEntity<T> res = restTemplate.exchange(uri, HttpMethod.GET, entity, clazz);
        return res.getBody();
    }


    public <T> T getObject(String endPoint, String token, Class<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(CommonConstants.HEADER_AUTH_NAME, CommonConstants.HEADER_AUTH_PREFIX + token);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<T> res = restTemplate.exchange(baseUrl + endPoint, HttpMethod.GET, entity, clazz);
        return res.getBody();
    }

    public <T> T getObject(String endPoint, String token, Map<String, ?> mapParameter, Class<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(CommonConstants.HEADER_AUTH_NAME, CommonConstants.HEADER_AUTH_PREFIX + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl + endPoint);
        for (Map.Entry<String, ?> entry : mapParameter.entrySet()) {
            uriComponentsBuilder = uriComponentsBuilder.queryParam(entry.getKey(), entry.getValue());
        }
        String uri = uriComponentsBuilder.build().toString();
        //uriComponentsBuilder.toUriString().replace("%20", " ");
        ResponseEntity<T> res = restTemplate.exchange(uri, HttpMethod.GET, entity, clazz, mapParameter);
        return res.getBody();

    }

    public <T> T getObjectPath(String endPoint, String token, Map<String, String> mapParameter, Class<T> clazz) {
        endPoint = convertUrlPathVariable(endPoint, mapParameter);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(CommonConstants.HEADER_AUTH_NAME, CommonConstants.HEADER_AUTH_PREFIX + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<T> res = restTemplate.exchange(baseUrl + endPoint, HttpMethod.GET, entity, clazz, mapParameter);
        return res.getBody();
    }

    public <T> T postObject(String endPoint, Object object, Class<T> clazz, MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(mediaType));
        headers.setContentType(mediaType);
        HttpEntity<Object> entity = new HttpEntity<>(object, headers);

        ResponseEntity<T> res = restTemplate.exchange(baseUrl + endPoint, HttpMethod.POST, entity, clazz, object);
        return res.getBody();
    }

    public <T> T postObject(String endPoint, Map<String, String> mapHeader, Object object, Class<T> clazz, MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(mediaType));
        headers.setContentType(mediaType);
        mapHeader.forEach(headers::set);
        HttpEntity<Object> entity = new HttpEntity<>(object, headers);

        ResponseEntity<T> res = restTemplate.exchange(baseUrl + endPoint, HttpMethod.POST, entity, clazz, object);
        return res.getBody();
    }


    public <T> T postObject(String endPoint, Class<T> clazz, Map<String, ?> mapObject) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<T> res = restTemplate.exchange(baseUrl + endPoint, HttpMethod.POST, entity, clazz, mapObject);
        return res.getBody();

    }

    public <T> T postObject(String endPoint, String token, Object object, Class<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(CommonConstants.HEADER_AUTH_NAME, CommonConstants.HEADER_AUTH_PREFIX + token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(object, headers);
        try {
            ResponseEntity<T> res = restTemplate.exchange(baseUrl + endPoint, HttpMethod.POST, entity, clazz);
            return res.getBody();
        } catch (HttpClientErrorException ex) {
            LOG.info("postObject bug: " + new String(ex.getResponseBodyAsByteArray()));
            throw ex;
        }

    }

    public <T> T postObject(String endPoint, String headerName, String contentHeader, Object object, Class<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(headerName, contentHeader);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(object, headers);
        try {
            ResponseEntity<T> res = restTemplate.exchange(baseUrl + endPoint, HttpMethod.POST, entity, clazz);
            return res.getBody();
        } catch (HttpClientErrorException ex) {
            LOG.info("postObject bug: " + new String(ex.getResponseBodyAsByteArray()));
            throw ex;
        }

    }

    public <T> T postObject(String endPoint, String headerName, String contentHeader, Object object, ParameterizedTypeReference<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(headerName, contentHeader);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(object, headers);
        try {
            ResponseEntity<T> res = restTemplate.exchange(baseUrl + endPoint, HttpMethod.POST, entity, clazz);
            return res.getBody();
        } catch (HttpClientErrorException ex) {
            LOG.info("postObject bug: " + new String(ex.getResponseBodyAsByteArray()));
            throw ex;
        }
    }

    public <T> T postObject(
            String endPoint, String headerName, String contentHeader,
            Map<String, String> mapParam,
            ParameterizedTypeReference<T> clazz
    ) {
        endPoint = convertUrlPathVariable(endPoint, mapParam);
        HttpHeaders headers = new HttpHeaders();
        headers.set(headerName, contentHeader);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>("parameters", headers);
        try {
            ResponseEntity<T> res = restTemplate.exchange(baseUrl + endPoint, HttpMethod.POST, entity, clazz, mapParam);
            return res.getBody();
        } catch (HttpClientErrorException ex) {
            LOG.info("postObject bug: " + new String(ex.getResponseBodyAsByteArray()));
            throw ex;
        }
    }

    public <T> T postObject(
            String endPoint, String headerName, String contentHeader,
            Object request, Map<String, String> mapParam,
            Class<T> clazz
    ) {
        endPoint = convertUrlPathVariable(endPoint, mapParam);
        HttpHeaders headers = new HttpHeaders();
        headers.set(headerName, contentHeader);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(request, headers);
        try {
            ResponseEntity<T> res = restTemplate.exchange(baseUrl + endPoint, HttpMethod.POST, entity, clazz);
            return res.getBody();
        } catch (HttpClientErrorException ex) {
            LOG.info("postObject bug: " + new String(ex.getResponseBodyAsByteArray()));
            throw ex;
        }
    }

    public <T> T postObject(
            String endPoint, String headerName, String contentHeader,
            Object request, Map<String, String> mapParam,
            ParameterizedTypeReference<T> clazz
    ) {
        endPoint = convertUrlPathVariable(endPoint, mapParam);
        HttpHeaders headers = new HttpHeaders();
        headers.set(headerName, contentHeader);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(request, headers);
        try {
            ResponseEntity<T> res = restTemplate.exchange(baseUrl + endPoint, HttpMethod.POST, entity, clazz);
            return res.getBody();
        } catch (HttpClientErrorException ex) {
            LOG.info("postObject bug: " + new String(ex.getResponseBodyAsByteArray()));
            throw ex;
        }
    }

    public <T> T postObject(String endPoint, Object object, ParameterizedTypeReference<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(object, headers);
        try {
            ResponseEntity<T> res = restTemplate.exchange(baseUrl + endPoint, HttpMethod.POST, entity, clazz);
            return res.getBody();
        } catch (HttpClientErrorException ex) {
            LOG.info("postObject bug: " + new String(ex.getResponseBodyAsByteArray()));
            throw ex;
        }

    }

    public <T> T postObject(String endPoint, String token, Class<T> clazz, Map<String, String> mapObject) {
        endPoint = convertUrlPathVariable(endPoint, mapObject);
        HttpHeaders headers = new HttpHeaders();
        headers.set(CommonConstants.HEADER_AUTH_NAME, CommonConstants.HEADER_AUTH_PREFIX + token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<T> res = restTemplate.exchange(baseUrl + endPoint, HttpMethod.POST, entity, clazz, mapObject);
        return res.getBody();

    }

    public <T> T putObject(String endPoint, Map<String, String> mapParameter, Object object, Class<T> clazz) {
        endPoint = convertUrlPathVariable(endPoint, mapParameter);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Object> entity = new HttpEntity<>(object, headers);
        try {
            ResponseEntity<T> res = restTemplate.exchange(baseUrl + endPoint, HttpMethod.PUT, entity, clazz);
            return res.getBody();
        } catch (HttpClientErrorException ex) {
            LOG.info("putObject bug: " + new String(ex.getResponseBodyAsByteArray()));
            throw ex;
        }

    }

    public <T> T putObject(String endPoint, String token, Object object, Class<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(CommonConstants.HEADER_AUTH_NAME, CommonConstants.HEADER_AUTH_PREFIX + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Object> entity = new HttpEntity<>(object, headers);
        try {
            ResponseEntity<T> res = restTemplate.exchange(baseUrl + endPoint, HttpMethod.PUT, entity, clazz);
            return res.getBody();
        } catch (HttpClientErrorException ex) {
            LOG.info("putObject bug: " + new String(ex.getResponseBodyAsByteArray()));
            throw ex;
        }

    }

    public <T> T putObject(
            String endPoint, String headerName, String contentHeader,
            Map<String, String> mapParameter,
            Class<T> clazz
    ) {
        endPoint = convertUrlPathVariable(endPoint, mapParameter);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(headerName, contentHeader);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            if (!mapParameter.isEmpty()) {
//                MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();
//                for (String key : mapParameter.keySet()) {
//                    bodyParams.add(key, mapParameter.get(key));
//                }
//
//                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//                HttpEntity<MultiValueMap<String, String>> entity =
//                        new HttpEntity<MultiValueMap<String, String>>(bodyParams, headers);

                UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl + endPoint);
                for (Map.Entry<String, ?> entry : mapParameter.entrySet()) {
                    uriComponentsBuilder = uriComponentsBuilder.queryParam(entry.getKey(), entry.getValue());
                }
                String uri = uriComponentsBuilder.build().toString();

                ResponseEntity<T> res = restTemplate.exchange(uri, HttpMethod.PUT, entity, clazz);
                return res.getBody();
            } else {
//                HttpEntity<Object> entity = new HttpEntity<>(headers);

                ResponseEntity<T> res = restTemplate.exchange(baseUrl + endPoint, HttpMethod.PUT, entity, clazz);
                return res.getBody();
            }
        } catch (HttpClientErrorException ex) {
            LOG.info("putObject bug: " + new String(ex.getResponseBodyAsByteArray()));
            throw ex;
        }
    }

    public <T> T putObject(
            String endPoint, String headerName, String contentHeader,
            Object request, Map<String, String> mapParam,
            Class<T> clazz
    ) {
        endPoint = convertUrlPathVariable(endPoint, mapParam);
        HttpHeaders headers = new HttpHeaders();
        headers.set(headerName, contentHeader);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(request, headers);
        try {
            ResponseEntity<T> res = restTemplate.exchange(baseUrl + endPoint, HttpMethod.PUT, entity, clazz);
            return res.getBody();
        } catch (HttpClientErrorException ex) {
            LOG.info("postObject bug: " + new String(ex.getResponseBodyAsByteArray()));
            throw ex;
        }
    }

    public <T> T patchObject(
            String endPoint, String headerName, String contentHeader, Object object, Class<T> clazz
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(headerName, contentHeader);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Object> entity = new HttpEntity<>(object, headers);
        ResponseEntity<T> res = restTemplate.exchange(baseUrl + endPoint, HttpMethod.PATCH, entity, clazz);
        return res.getBody();
    }

    public <T> T deleteObject(
            String endPoint, String token, Map<String, String> mapParameter, Class<T> clazz
    ) {
        endPoint = convertUrlPathVariable(endPoint, mapParameter);

        HttpHeaders headers = new HttpHeaders();
        headers.set(CommonConstants.HEADER_AUTH_NAME, CommonConstants.HEADER_AUTH_PREFIX + token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Object> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<T> res = restTemplate.exchange(baseUrl + endPoint, HttpMethod.DELETE, entity, clazz);
        return res.getBody();
    }

    public <T> T deleteObject(
            String endPoint, String token, Object object, Class<T> clazz
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(CommonConstants.HEADER_AUTH_NAME, CommonConstants.HEADER_AUTH_PREFIX + token);
//        headers.set(headerName, contentHeader);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Object> entity = new HttpEntity<>(object, headers);
        ResponseEntity<T> res = restTemplate.exchange(baseUrl + endPoint, HttpMethod.DELETE, entity, clazz);
        return res.getBody();
    }

    public static String convertUrlPathVariable(String url, Map<String, String> pars) {
        Set<String> keys = pars.keySet();
        List<String> keyArr = new ArrayList<>(keys);
        for (String key : keyArr) {
            String temKey = "{" + key + "}";
            if (url.contains(temKey)) {
                url = url.replace(temKey, pars.get(key));
                pars.remove(key);
            }
        }
        return url;
    }

    public <T> T postObjectFormUrlencode(String endpoint, Map<String, String> params, Class<T> responseModel) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        params.entrySet().stream()
                .map(e -> body.put(e.getKey(), Arrays.asList(e.getValue())))
                .collect(Collectors.toList());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(body, headers);


        ResponseEntity<T> res = restTemplate.exchange(baseUrl + endpoint, HttpMethod.POST, request, responseModel);
        return res.getBody();
    }
}
