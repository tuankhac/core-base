package com.vmo.core.configs.env;

import lombok.Getter;

public enum Environments {
    LOCAL("Local"),
    UNIT_TEST("Unit test"),
    DEV("Development"),
    STAGING("Staging"),
    BETA("Beta"),
    PRODUCTION("Production");

    @Getter
    String value;

    Environments(String value) {
        this.value = value;
    }
}
