package com.vmo.core.modules.repositories.job;

import com.vmo.core.modules.models.database.entities.shared.job.ScheduleJobLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

public interface ScheduleJobLogRepository extends JpaRepository<ScheduleJobLog, Long> {
    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM schedule_job_log " +
            "JOIN schedule_job_config ON schedule_job_log.job_id = schedule_job_config.id " +
            "WHERE (:jobId IS NULL OR :jobId = job_id)" +
            "ORDER BY id OFFSET :offset ROWS FETCH NEXT :size ROWS ONLY " +
            "AND schedule_job_config.service = :#{@commonConfig.getService()} "
    )
    List<ScheduleJobLog> findAll(
            @Param("jobId") Long jobId,
            @Param("offset") long offset,
            @Param("size") int size
    );

    @Query(nativeQuery = true, value = "SELECT COUNT(*) " +
            "FROM schedule_job_log " +
            "JOIN schedule_job_config ON schedule_job_log.job_id = schedule_job_config.id " +
            "WHERE (:jobId IS NULL OR :jobId = job_id) " +
            "AND schedule_job_config.service = :#{@commonConfig.getService()} "
    )
    int countAll(@Param("jobId") Long jobId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(nativeQuery = true, value = "DELETE schedule_job_log " +
            "FROM schedule_job_log " +
            "JOIN schedule_job_config ON schedule_job_log.job_id = schedule_job_config.id " +
            "WHERE (:jobId IS NULL OR :jobId = job_id) " +
            "AND start_time <= :olderThan " +
            "AND schedule_job_config.service = :#{@commonConfig.getService()} "
    )
    int purgeLogs(
            @Param("jobId") Long jobId,
            @Param("olderThan") Timestamp olderThan
    );
}
