package com.vmo.core.modules.models.database.types.job;

import lombok.Getter;

public enum JobTimeType {
    FIX_RATE("Fixed rate"),
    FIX_DELAY("Fixed delay"),
    CRON("Cron");

    @Getter
    private final String value;

    JobTimeType(String value) {
        this.value = value;
    }

    public static JobTimeType findByValue(String value) {
        for (JobTimeType type : JobTimeType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }

        return null;
    }
}
