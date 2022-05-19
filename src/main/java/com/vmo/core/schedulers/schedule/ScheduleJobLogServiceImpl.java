package com.vmo.core.schedulers.schedule;

import com.vmo.core.common.exception.ExceptionResponse;
import com.vmo.core.modules.managers.schedule.ScheduleJobLogManager;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ScheduleJobLogServiceImpl implements ScheduleJobLogService {
    @Autowired
    private ScheduleJobLogManager manager;

    public Object getLogs(Long jobId, Pageable pageable) throws ExceptionResponse {
        return manager.getLogs(jobId, pageable);
    }

    public Object purgeLog(Long jobId, LocalDateTime olderThan) throws ExceptionResponse {
        return manager.purgeLog(jobId, olderThan);
    }
}
