package com.vmo.core.logging.repositories;

import com.vmo.core.modules.models.database.entities.shared.LogApiError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Deprecated
//TODO refactor to new logger
public interface LogApiErrorRepository extends JpaRepository<LogApiError, Long> {
}
