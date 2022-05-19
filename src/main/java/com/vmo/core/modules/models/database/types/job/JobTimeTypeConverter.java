package com.vmo.core.modules.models.database.types.job;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class JobTimeTypeConverter implements AttributeConverter<JobTimeType, String> {
    @Override
    public JobTimeType convertToEntityAttribute(String value) {
        return JobTimeType.findByValue(value);
    }

    @Override
    public String convertToDatabaseColumn(JobTimeType jobTimeType) {
        if (jobTimeType == null) return null;
        return jobTimeType.getValue();
    }
}
