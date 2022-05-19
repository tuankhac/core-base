package com.vmo.core.schedulers.job;

import com.vmo.core.configs.CommonConfig;
import com.vmo.core.modules.models.database.entities.shared.job.ScheduleJobConfig;
import com.vmo.core.modules.repositories.job.ScheduleJobConfigRepository;
import com.vmo.core.schedulers.JobFactory;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public abstract class ScheduledJob implements Runnable, Trigger {
    @Autowired
    protected JobFactory jobFactory;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private ScheduleJobConfigRepository scheduleJobConfigRepository;

    private ScheduleJobConfig config;
    private boolean isRunning = false;
    private boolean forcedRun;
    private boolean isFirstRun = false;
    private int firstRunDelay = 0; //seconds
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private LocalDateTime failTime;
    private LocalDateTime cancelTime;
    @Getter
    private boolean isFail;
    @Getter
    private LocalDateTime lastQueueTime;
    @Getter
    private LocalDateTime lastQueueToRunAt;
    @Getter
    @Setter
    private String customLogString;
    @Getter
    @Setter
    private Float customLogNumber;
    private Trigger trigger;
    private TriggerContext lastTriggerContext;

    @Override
    public final Date nextExecutionTime(TriggerContext triggerContext) {
        lastTriggerContext = triggerContext;
        if (JobFactory.isShuttingDown() || config == null) return null; //when shutting down, dont queue new task

        try {
            if (trigger == null) return null;

            lastQueueTime = LocalDateTime.now();
            Date queueToRunAt;
            if (forcedRun) {
                forcedRun = false;
                queueToRunAt = new Date();
            } else {
                queueToRunAt = trigger.nextExecutionTime(triggerContext);
            }
            lastQueueToRunAt = LocalDateTime.fromDateFields(queueToRunAt);
            if (isFirstRun) {
                lastQueueToRunAt.plusSeconds(firstRunDelay);
                isFirstRun = false;
            }

            return lastQueueToRunAt.toDate();
        } catch (Exception e) {
            return null; //never queue
        }
    }

    /**
     * When an unhandled exception was thrown
     *
     * @param throwable
     */
    protected abstract void onFail(Throwable throwable);

    public void buildTrigger() {
        buildTrigger(0, false);
    }

    public void buildTrigger(int firstRunDelay, boolean forceRun) {
        if (config == null) return;
        this.firstRunDelay = firstRunDelay;
        forcedRun = forceRun;
        switch (config.getJobTimeType()) {
            case FIX_DELAY:
                if (config.getJobTimeSeconds() != null) {
                    trigger = new PeriodicTrigger(config.getJobTimeSeconds(), TimeUnit.SECONDS);
                }
                break;
            case FIX_RATE:
                if (config.getJobTimeSeconds() != null) {
                    trigger = new PeriodicTrigger(config.getJobTimeSeconds(), TimeUnit.SECONDS);
                    ((PeriodicTrigger) trigger).setFixedRate(true);
                }
                break;
            case CRON:
                if (!StringUtils.isBlank(config.getJobTimeCron()) &&
                        CronSequenceGenerator.isValidExpression(config.getJobTimeCron())
                ) {
                    trigger = new CronTrigger(config.getJobTimeCron());
                }
                break;
        }
    }

    public void disable() {
        if (config == null) {
            ScheduleJobConfig config = scheduleJobConfigRepository.findOne(this.getClass().getSimpleName());
        }
        if (config != null) {
            if (!config.isEnable()) return;
            jobFactory.updateEnable(this, false);
            trigger = null;
            config.setEnable(false);
            config = scheduleJobConfigRepository.save(config);
        }
    }

    public void enable() {
        if (config == null) {
            ScheduleJobConfig config = scheduleJobConfigRepository.findOne(this.getClass().getSimpleName());
        }
        if (config != null) {
            if (config.isEnable()) return;
            jobFactory.updateEnable(this, true);
            config.setEnable(true);
            config = scheduleJobConfigRepository.save(config);
        }
    }

    public final boolean isEnable() {
        return config != null && config.isEnable();
    }

    public final ScheduleJobConfig getConfig() {
        return config;
    }

    public final void setConfig(ScheduleJobConfig config) {
        this.config = config;
    }

    public final boolean isRunning() {
        return isRunning;
    }

    protected final void setRunning(boolean running) {
        isRunning = running;
    }

    public final LocalDateTime getStartTime() {
        return startTime;
    }

    protected final void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public final LocalDateTime getFinishTime() {
        return finishTime;
    }

    protected final void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }

    public final LocalDateTime getFailTime() {
        return failTime;
    }

    protected final void setFailTime(LocalDateTime failTime) {
        this.failTime = failTime;
    }

    public final LocalDateTime getCancelTime() {
        return cancelTime;
    }

    protected final void setCancelTime(LocalDateTime cancelTime) {
        this.cancelTime = cancelTime;
    }

    protected final void setFail(boolean isFail) {
        this.isFail = isFail;
    }

    protected Trigger getTrigger() {
        return trigger;
    }

    protected TriggerContext getLastTriggerContext() {
        return lastTriggerContext;
    }
}
