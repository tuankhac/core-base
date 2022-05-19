package com.vmo.core.logging.repositories;

import com.vmo.core.modules.models.database.entities.shared.log.LogAsyncExclude;

import java.util.List;

public interface LogAsyncExcludeRepositoryExtended {
    List<LogAsyncExclude> findAllExcludedLogs();
}
