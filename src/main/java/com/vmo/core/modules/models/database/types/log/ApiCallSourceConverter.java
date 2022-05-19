package com.vmo.core.modules.models.database.types.log;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ApiCallSourceConverter implements AttributeConverter<ApiCallSource, String> {
    @Override
    public String convertToDatabaseColumn(ApiCallSource apiCallSource) {
        if (apiCallSource != null) {
            return apiCallSource.getValue();
        }
        return null;
    }

    @Override
    public ApiCallSource convertToEntityAttribute(String s) {
        return ApiCallSource.findbyValue(s);
    }
}
