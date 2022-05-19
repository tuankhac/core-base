package com.vmo.core.modules.models.database.types.converter;

import org.springframework.http.HttpMethod;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class HttpMethodConverter implements AttributeConverter<HttpMethod, String> {
    @Override
    public String convertToDatabaseColumn(HttpMethod httpMethod) {
        if (httpMethod != null) {
            return httpMethod.name();
        }
        return null;
    }

    @Override
    public HttpMethod convertToEntityAttribute(String s) {
        return HttpMethod.resolve(s);
    }
}
