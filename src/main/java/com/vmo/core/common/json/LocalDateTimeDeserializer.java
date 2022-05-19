package com.vmo.core.common.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.vmo.core.common.CommonConstants;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern(CommonConstants.FORMAT_DATE_TIME);
    private static final DateTimeFormatter FORMATTER_2 = DateTimeFormat.forPattern(CommonConstants.FORMAT_DATE_TIME_EBAY);

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        String content = jsonParser.getValueAsString();
        if (content == null || content.trim().equals("")) {
            return null;
        }
        try {
            return LocalDateTime.parse(content, FORMATTER);
        } catch (IllegalArgumentException e) {
            return LocalDateTime.parse(content, FORMATTER_2);
        }

    }
}
