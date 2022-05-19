package com.vmo.core.schedulers.job;

import com.vmo.core.common.CommonResponseMessages;
import com.vmo.core.schedulers.handler.JobLogHandler;
import lombok.Getter;
import org.joda.time.LocalDateTime;

public class JobExecutor implements Runnable {
    private final JobLogHandler handler;
    @Getter
    private final ScheduledJob job;

    public JobExecutor(JobLogHandler handler, ScheduledJob job) {
        if (handler == null || job == null) {
            throw new NullPointerException();
        }
        this.handler = handler;
        this.job = job;
    }

    @Override
    public void run() {
        try {
            if (job.isRunning()) {
                throw new IllegalStateException(CommonResponseMessages.JOB_RUNNING_SAME_TIME);
            }
            job.setRunning(true);
            job.setStartTime(LocalDateTime.now());

            job.run();

            if (job.isFail()) {
                job.setFailTime(LocalDateTime.now());
                handler.logJobError(job);
            } else {
                job.setFinishTime(LocalDateTime.now());
                handler.jogJobSuccess(job);
            }
        } catch (Throwable throwable) {
            job.setFailTime(LocalDateTime.now());
            try {
                job.onFail(throwable);
            } catch (Throwable t) { //death of dead
                t.printStackTrace();
            } finally {
                handler.logJobError(throwable, job);
            }
        } finally {
            job.setRunning(false);
            job.setFail(false);
            job.setStartTime(null);
            job.setFinishTime(null);
            job.setCancelTime(null);
            job.setFailTime(null);
            job.setCustomLogString(null);
            job.setCustomLogNumber(null);
        }
    }

    public void forcedCancel() {
        job.setCancelTime(LocalDateTime.now());
    }
}
