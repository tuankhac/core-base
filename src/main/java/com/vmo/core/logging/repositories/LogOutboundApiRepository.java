package com.vmo.core.logging.repositories;

import com.vmo.core.modules.models.database.entities.shared.log.LogOutboundApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogOutboundApiRepository extends JpaRepository<LogOutboundApi, Long> {
}
