package com.vmo.core.schedulers.schedule;

import com.vmo.core.common.exception.ExceptionResponse;
import com.vmo.core.modules.models.requests.schedule.ScheduleJobConfigUpdateRequest;

public interface ScheduleJobConfigService {
    Object getJobConfigs(Boolean isEnable);

    Object getRunningJobs();

    Object getInqueueJobs();

    Object getJobDetail(long id) throws ExceptionResponse;

    Object updateJobConfig(
            long id,
            ScheduleJobConfigUpdateRequest request
    ) throws ExceptionResponse;
}
