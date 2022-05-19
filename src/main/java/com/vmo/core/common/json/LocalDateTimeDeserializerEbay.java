package com.vmo.core.common.json;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.vmo.core.common.CommonConstants;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

public class LocalDateTimeDeserializerEbay extends JsonDeserializer<LocalDateTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern(CommonConstants.FORMAT_DATE_TIME_EBAY);


    @Override
    public LocalDateTime deserialize(com.fasterxml.jackson.core.JsonParser jsonParser, com.fasterxml.jackson.databind.DeserializationContext deserializationContext) throws IOException, com.fasterxml.jackson.core.JsonProcessingException {
        String value = jsonParser.getValueAsString();
        if (value == null || value.trim().equals("")) {
            return null;
        }
        return LocalDateTime.parse(value, FORMATTER);
    }

}
