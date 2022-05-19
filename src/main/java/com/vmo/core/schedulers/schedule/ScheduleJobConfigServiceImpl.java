package com.vmo.core.schedulers.schedule;

import com.vmo.core.common.exception.ExceptionResponse;
import com.vmo.core.modules.managers.schedule.ScheduleJobConfigManager;
import com.vmo.core.modules.models.requests.schedule.ScheduleJobConfigUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleJobConfigServiceImpl implements ScheduleJobConfigService {
    @Autowired
    private ScheduleJobConfigManager manager;

    @Override
    public Object getJobConfigs(Boolean isEnable) {
        return manager.getJobConfigs(isEnable);
    }

    @Override
    public Object getRunningJobs() {
        return manager.getRunningJobs();
    }

    @Override
    public Object getInqueueJobs() {
        return manager.getInqueueJobs();
    }

    @Override
    public Object getJobDetail(long id) throws ExceptionResponse {
        return manager.getJobDetail(id);
    }

    @Override
    public Object updateJobConfig(long id, ScheduleJobConfigUpdateRequest request) throws ExceptionResponse {
        return manager.updateJobConfig(id, request);
    }
}
