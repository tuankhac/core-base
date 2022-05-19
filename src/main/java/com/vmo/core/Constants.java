package com.vmo.core;

public interface Constants {
     class Links {
        public static final String BASE_PACKAGE = "com.vmo.core.";
        public static final String MODULE_PACKAGE = BASE_PACKAGE + "modules.";
        public static final String SCHEDULER_PACKAGE = BASE_PACKAGE + "schedulers";
        public static final String COMMON_PACKAGE = BASE_PACKAGE + "common";
        public static final String FILTERS_PACKAGE = BASE_PACKAGE + "filters";
//        public static final String CONTROLLERS_PACKAGE = MODULE_PACKAGE + "controllers";
//        public static final String SERVICES_PACKAGE = MODULE_PACKAGE + "services";
        public static final String MANAGERS_PACKAGE = MODULE_PACKAGE + "managers";
        public static final String REPOSITORIES_PACKAGE = MODULE_PACKAGE + "repositories";
        public static final String MODELS_PACKAGE = MODULE_PACKAGE + "models";

        public static final String INTEGRATION_PACKAGE = BASE_PACKAGE + "infras";
        public static final String LOGGING_PACKAGE = BASE_PACKAGE + "logging";
        public static final String SECURITY_PACKAGE = BASE_PACKAGE + "configs.security";
        public static final String MICROSERVICES_PACKAGE = BASE_PACKAGE + "microservices";
//        public static final String CONTROLLERS_SWAGGER_PACKAGE = MODULE_PACKAGE + "controllers.*";
    }

    public static final String SCHEMA_DBO = "dbo";
}
