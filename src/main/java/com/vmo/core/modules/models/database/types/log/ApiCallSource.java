package com.vmo.core.modules.models.database.types.log;

import lombok.Getter;

public enum ApiCallSource {
    INBOUND("Inbound"),
    OUTBOUND("Outbound");

    @Getter
    private String value;

    ApiCallSource(String value) {
        this.value = value;
    }

    public static ApiCallSource findbyValue(String value) {
        if (value == null) {
            return null;
        }

        for (ApiCallSource source : ApiCallSource.values()) {
            if (source.getValue().equals(value)) {
                return source;
            }
        }

        return null;
    }
}
