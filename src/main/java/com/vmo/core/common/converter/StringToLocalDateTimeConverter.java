package com.vmo.core.common.converter;

import com.vmo.core.common.CommonConstants;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern(CommonConstants.FORMAT_DATE_TIME);

    @Override
    public LocalDateTime convert(String s) {
        if (s == null) {
            return null;
        }
        return LocalDateTime.parse(s, FORMATTER);
    }
}
