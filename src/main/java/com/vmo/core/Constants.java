package com.vmo.core;

public interface Constants {
     String BASE_PACKAGE = "com.vmo.core.";
     String MODULE_PACKAGE = BASE_PACKAGE + "modules.";
     String SCHEDULER_PACKAGE = BASE_PACKAGE + "schedulers";
     String COMMON_PACKAGE = BASE_PACKAGE + "common";
     String FILTERS_PACKAGE = BASE_PACKAGE + "filters";
//         String CONTROLLERS_PACKAGE = MODULE_PACKAGE + "controllers";
//         String SERVICES_PACKAGE = MODULE_PACKAGE + "services";
     String MANAGERS_PACKAGE = MODULE_PACKAGE + "managers";
     String REPOSITORIES_PACKAGE = MODULE_PACKAGE + "repositories";
     String MODELS_PACKAGE = MODULE_PACKAGE + "models";

     String INTEGRATION_PACKAGE = BASE_PACKAGE + "infras";
     String LOGGING_PACKAGE = BASE_PACKAGE + "logging";
     String SECURITY_PACKAGE = BASE_PACKAGE + "configs.security";
     String MICROSERVICES_PACKAGE = BASE_PACKAGE + "microservices";
     String CONTROLLERS_SWAGGER_PACKAGE = MODULE_PACKAGE + "controllers.*";
    

     String SCHEMA_DBO = "dbo";
}
