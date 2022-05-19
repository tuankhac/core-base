package com.vmo.core.modules.models.response.schedule;

import com.vmo.core.modules.models.database.entities.shared.job.ScheduleJobConfig;
import com.vmo.core.modules.models.database.types.job.JobTimeType;
import org.joda.time.LocalDateTime;

public class ScheduleJobConfigDetailResponse {
    private long id;
    private String jobType;
    private boolean isEnable;
    private JobTimeType jobTimeType;
    private Long jobTimeSeconds;
    private String jobTimeCron;
    private LocalDateTime createdTime;
    private LocalDateTime lastUpdate;
    private boolean isDelete;
    private Boolean logFailEnable;
    private Boolean logCancelEnable;
    private Boolean logSuccessEnable;
    private Boolean isRunning;
    private LocalDateTime queueTime;
    private LocalDateTime startTime;
    private LocalDateTime nextRun;

    public ScheduleJobConfigDetailResponse() {}

    public ScheduleJobConfigDetailResponse(ScheduleJobConfig config) {
        id = config.getId();
        jobType = config.getJobType();
        isEnable = config.isEnable();
        jobTimeType = config.getJobTimeType();
        jobTimeSeconds = config.getJobTimeSeconds();
        jobTimeCron = config.getJobTimeCron();
        createdTime = config.getCreatedTime();
        lastUpdate = config.getLastUpdate();
        isDelete = config.isDelete();
        logFailEnable = config.getLogFailEnable();
        logCancelEnable = config.getLogCancelEnable();
        logSuccessEnable = config.getLogSuccessEnable();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public JobTimeType getJobTimeType() {
        return jobTimeType;
    }

    public void setJobTimeType(JobTimeType jobTimeType) {
        this.jobTimeType = jobTimeType;
    }

    public Long getJobTimeSeconds() {
        return jobTimeSeconds;
    }

    public void setJobTimeSeconds(Long jobTimeSeconds) {
        this.jobTimeSeconds = jobTimeSeconds;
    }

    public String getJobTimeCron() {
        return jobTimeCron;
    }

    public void setJobTimeCron(String jobTimeCron) {
        this.jobTimeCron = jobTimeCron;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public Boolean getLogFailEnable() {
        return logFailEnable;
    }

    public void setLogFailEnable(Boolean logFailEnable) {
        this.logFailEnable = logFailEnable;
    }

    public Boolean getLogCancelEnable() {
        return logCancelEnable;
    }

    public void setLogCancelEnable(Boolean logCancelEnable) {
        this.logCancelEnable = logCancelEnable;
    }

    public Boolean getLogSuccessEnable() {
        return logSuccessEnable;
    }

    public void setLogSuccessEnable(Boolean logSuccessEnable) {
        this.logSuccessEnable = logSuccessEnable;
    }

    public Boolean getRunning() {
        return isRunning;
    }

    public void setRunning(Boolean running) {
        isRunning = running;
    }

    public LocalDateTime getQueueTime() {
        return queueTime;
    }

    public void setQueueTime(LocalDateTime queueTime) {
        this.queueTime = queueTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getNextRun() {
        return nextRun;
    }

    public void setNextRun(LocalDateTime nextRun) {
        this.nextRun = nextRun;
    }
}
