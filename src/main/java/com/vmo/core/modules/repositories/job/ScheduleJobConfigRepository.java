package com.vmo.core.modules.repositories.job;

import com.vmo.core.modules.models.database.entities.shared.job.ScheduleJobConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleJobConfigRepository extends JpaRepository<ScheduleJobConfig, Long> {
    @Query(nativeQuery = true, value = "SELECT TOP 1 * " +
            "FROM schedule_job_config " +
            "WHERE job_type = :#{#type} " +
            "AND is_delete = 0 " +
            "AND service = :#{@commonConfig.getService()} "
    )
    ScheduleJobConfig findOne(
            @Param("type") String type
    );

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM schedule_job_config " +
            "WHERE is_delete = 0 " +
            "AND service = :#{@commonConfig.getService()} "
    )
    List<ScheduleJobConfig> findAll();

    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM schedule_job_config " +
            "WHERE is_delete = 0 " +
            "AND (:isEnable IS NULL OR is_enable = :isEnable) " +
            "AND service = :#{@commonConfig.getService()} "
    )
    List<ScheduleJobConfig> findAll(
            @Param("isEnable") Boolean isEnable
    );
}
