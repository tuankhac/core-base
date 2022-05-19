package com.vmo.core.common.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.vmo.core.common.CommonConstants;
import org.joda.time.YearMonth;

import java.io.IOException;

public class YearMonthSerializer extends JsonSerializer<YearMonth> {
    @Override
    public void serialize(YearMonth yearMonth, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException, JsonProcessingException {
        jsonGenerator.writeString(yearMonth.toString(CommonConstants.FORMAT_MONTH));
    }
}
