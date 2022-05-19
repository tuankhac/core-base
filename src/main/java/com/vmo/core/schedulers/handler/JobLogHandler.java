package com.vmo.core.schedulers.handler;

import com.vmo.core.modules.models.database.entities.shared.job.ScheduleJobLog;
import com.vmo.core.modules.repositories.job.ScheduleJobConfigRepository;
import com.vmo.core.modules.repositories.job.ScheduleJobLogRepository;
import com.vmo.core.schedulers.job.ScheduledJob;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobLogHandler {
    @Autowired
    private ScheduleJobLogRepository scheduleJobLogRepository;
    @Autowired
    private ScheduleJobConfigRepository scheduleJobConfigRepository;
    @Autowired
    private JobFailHandler jobFailHandler;

    public void logJobError(ScheduledJob scheduledJob) {
        logJobError(null, scheduledJob);
    }

    public void logJobError(Throwable throwable, ScheduledJob scheduledJob) {
        if (throwable != null) {
            throwable.printStackTrace();
        }

        if (scheduledJob == null) return;

        try {
            if (scheduledJob.getConfig().getLogFailEnable() == null ||
                    !scheduledJob.getConfig().getLogFailEnable()) {
                return;
            }

            ScheduleJobLog log = new ScheduleJobLog();
            log.setJobId(scheduledJob.getConfig().getId());
            log.setStartTime(scheduledJob.getStartTime());
            log.setFailTime(scheduledJob.getFailTime());
            log.setCustomLogString(scheduledJob.getCustomLogString());
            log.setCustomLogNumber(scheduledJob.getCustomLogNumber());
            if (throwable != null) {
                String exception = ExceptionUtils.getStackTrace(throwable);
                log.setStackTrace(exception);
            }

            scheduleJobLogRepository.save(log);
        } catch (Exception e) {
            jobFailHandler.handleError(e);
        }
    }

    public void jogJobSuccess(ScheduledJob scheduledJob) {
        if (scheduledJob == null) return;

        try {
            if (scheduledJob.getConfig().getLogSuccessEnable() == null ||
                    !scheduledJob.getConfig().getLogSuccessEnable()) {
                return;
            }

            ScheduleJobLog log = new ScheduleJobLog();
            log.setJobId(scheduledJob.getConfig().getId());
            log.setStartTime(scheduledJob.getStartTime());
            log.setFinishTime(scheduledJob.getFinishTime());
            log.setCustomLogString(scheduledJob.getCustomLogString());
            log.setCustomLogNumber(scheduledJob.getCustomLogNumber());

            scheduleJobLogRepository.save(log);
        } catch (Exception e) {
            jobFailHandler.handleError(e);
        }
    }

    public void logJobCancel(ScheduledJob scheduledJob) {
        if (scheduledJob == null) return;

        try {
            if (scheduledJob.getConfig().getLogCancelEnable() == null ||
                    !scheduledJob.getConfig().getLogCancelEnable()) {
                return;
            }

            ScheduleJobLog log = new ScheduleJobLog();
            log.setJobId(scheduledJob.getConfig().getId());
            log.setStartTime(scheduledJob.getStartTime());
            log.setCancelTime(scheduledJob.getCancelTime());
            log.setCustomLogString(scheduledJob.getCustomLogString());
            log.setCustomLogNumber(scheduledJob.getCustomLogNumber());

            scheduleJobLogRepository.save(log);
        } catch (Exception e) {
            jobFailHandler.handleError(e);
        }
    }
}
