package com.vmo.core.logging.repositories;

import com.vmo.core.modules.models.database.entities.shared.log.LogApiConfig;
import com.vmo.core.modules.models.database.types.log.ApiCallSource;

import java.util.List;

public interface LogApiConfigRepositoryExtended {
    List<LogApiConfig> findAllActive(ApiCallSource apiCallSource);
}
