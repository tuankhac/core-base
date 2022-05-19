package com.vmo.core.configs.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmo.core.common.CommonConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.MediaType;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CacheServletRequestWrapper extends HttpServletRequestWrapper {
    public static final String TEXT_XML_TYPE = "text/xml;charset=utf-8";

    private CacheServletInputStream cacheIn = null;
    private final ObjectMapper objectMapper;
    private final String environment;

    public CacheServletRequestWrapper(HttpServletRequest request, ObjectMapper objectMapper, String environment) {
        super(request);
        this.objectMapper = objectMapper;
        this.environment = environment;
    }


    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (cacheIn != null) {
            return cacheIn;
        }
        ServletInputStream in = super.getInputStream();
        String contentType = getContentType();
//        if (MediaType.APPLICATION_JSON_VALUE.equals(contentType) || TEXT_XML_TYPE.equals(contentType)) {
        if (CommonConstants.cacheableBodyContentTypes.stream()
                .anyMatch(type -> StringUtils.containsIgnoreCase(contentType, type))
        ) {
            ByteArrayOutputStream ar = new ByteArrayOutputStream();
            IOUtils.copy(in, ar);
            byte[] b = ar.toByteArray();
            String content = new String(b, 0, b.length);

            if (MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
                try {
                    Object o = objectMapper.readValue(content, Object.class);
                    content = objectMapper.writeValueAsString(o);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            ByteArrayInputStream intem = new ByteArrayInputStream(ar.toByteArray());
            cacheIn = new CacheServletInputStream(intem, b.length, content);
            if (!(environment != null && environment.equals("production"))) {
                System.out.println("content: " + content);
            }
            return cacheIn;
        }
        return in;
    }
}
