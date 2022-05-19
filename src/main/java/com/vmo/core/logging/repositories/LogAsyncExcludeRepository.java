package com.vmo.core.logging.repositories;

import com.vmo.core.modules.models.database.entities.shared.log.LogAsyncExclude;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogAsyncExcludeRepository extends JpaRepository<LogAsyncExclude, Long>, LogAsyncExcludeRepositoryExtended {
}
