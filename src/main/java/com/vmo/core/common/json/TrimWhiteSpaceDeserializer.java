package com.vmo.core.common.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;

import java.io.IOException;

public class TrimWhiteSpaceDeserializer extends StringDeserializer {
    private static final long serialVersionUID = -3562065572263950443L;

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getValueAsString();
        return value == null ? null : value.trim();
    }
}
