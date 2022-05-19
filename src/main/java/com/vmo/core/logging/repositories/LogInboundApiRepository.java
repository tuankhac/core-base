package com.vmo.core.logging.repositories;

import com.vmo.core.modules.models.database.entities.shared.log.LogInboundApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogInboundApiRepository extends JpaRepository<LogInboundApi, Long> {
}
