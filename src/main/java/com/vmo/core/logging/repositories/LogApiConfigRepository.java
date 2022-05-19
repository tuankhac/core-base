package com.vmo.core.logging.repositories;

import com.vmo.core.modules.models.database.entities.shared.log.LogApiConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogApiConfigRepository extends JpaRepository<LogApiConfig, Long>, LogApiConfigRepositoryExtended {
}
