package com.vmo.core.schedulers.schedule;

import com.vmo.core.common.exception.ExceptionResponse;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Pageable;

public interface ScheduleJobLogService {
    Object getLogs(Long jobId, Pageable pageable) throws ExceptionResponse;

    Object purgeLog(Long jobId, LocalDateTime olderThan) throws ExceptionResponse;
}
