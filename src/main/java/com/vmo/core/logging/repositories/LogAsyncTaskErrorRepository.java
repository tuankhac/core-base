package com.vmo.core.logging.repositories;

import com.vmo.core.modules.models.database.entities.shared.log.LogAsyncTaskError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogAsyncTaskErrorRepository extends JpaRepository<LogAsyncTaskError, Long> {
}
