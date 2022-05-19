package com.vmo.core.modules.models.requests.schedule;

import com.vmo.core.modules.models.database.types.job.JobTimeType;
import com.vmo.core.modules.models.requests.schedule.detail.JobConfigCron;
import io.swagger.annotations.ApiModelProperty;

public class ScheduleJobConfigUpdateRequest {
    private Boolean isEnable;
    private JobTimeType jobTimeType;
    private Long jobTimeSeconds;
    @ApiModelProperty(notes = "either full cron or build support must be provided if time type is cron")
    private JobConfigCron jobTimeCron;
    private Boolean logFailEnable;
    private Boolean logCancelEnable;
    private Boolean logSuccessEnable;

    public Boolean getEnable() {
        return isEnable;
    }

    public void setEnable(Boolean enable) {
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

    public JobConfigCron getJobTimeCron() {
        return jobTimeCron;
    }

    public void setJobTimeCron(JobConfigCron jobTimeCron) {
        this.jobTimeCron = jobTimeCron;
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
}
