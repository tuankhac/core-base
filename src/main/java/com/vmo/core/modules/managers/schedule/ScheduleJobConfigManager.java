package com.vmo.core.modules.managers.schedule;

import com.vmo.core.common.CommonResponseMessages;
import com.vmo.core.common.exception.ExceptionResponse;
import com.vmo.core.common.utils.schedule.ScheduleUtils;
import com.vmo.core.modules.models.database.entities.shared.job.ScheduleJobConfig;
import com.vmo.core.modules.models.database.types.job.JobTimeType;
import com.vmo.core.modules.models.requests.schedule.ScheduleJobConfigUpdateRequest;
import com.vmo.core.modules.models.requests.schedule.detail.JobConfigCronSupport;
import com.vmo.core.modules.models.response.ErrorCode;
import com.vmo.core.modules.models.response.schedule.ScheduleJobConfigDetailResponse;
import com.vmo.core.modules.repositories.job.ScheduleJobConfigRepository;
import com.vmo.core.schedulers.JobFactory;
import com.vmo.core.schedulers.job.ScheduledJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ScheduleJobConfigManager {
    @Autowired
    private JobFactory jobFactory;
    @Autowired
    private ScheduleJobConfigRepository scheduleJobConfigRepository;

    public Object getJobConfigs(Boolean isEnable) {
        return scheduleJobConfigRepository.findAll(isEnable);
    }

    public Object getRunningJobs() {
        return jobFactory.getRunningJobConfig();
    }

    public Object getInqueueJobs() {
        return jobFactory.getInqueueJob().stream()
                .map(job -> {
                    ScheduleJobConfigDetailResponse res = new ScheduleJobConfigDetailResponse(job.getConfig());
                    res.setQueueTime(job.getLastQueueTime());
                    res.setNextRun(job.getLastQueueToRunAt());
                    return res;
                }).collect(Collectors.toList());
    }

    public Object getJobDetail(long id) throws ExceptionResponse {
        ScheduleJobConfig config = findJobConfig(id);
        ScheduledJob job = jobFactory.getJob(config.getJobType());
        if (job == null) {
            config.setDelete(true);
            scheduleJobConfigRepository.save(config);
            throw new ExceptionResponse(
                    CommonResponseMessages.JOB_CONFIG_OUTDATED,
                    ErrorCode.RESOURCE_NOT_FOUND
            );
        }

        ScheduleJobConfigDetailResponse response = new ScheduleJobConfigDetailResponse(config);
        if (config.isEnable()) {
            response.setRunning(job.isRunning());
            response.setQueueTime(job.getLastQueueTime());
            if (job.isRunning()) {
                response.setStartTime(job.getStartTime());
            } else {
                response.setNextRun(job.getLastQueueToRunAt());
            }
        }
        return response;
    }

    public Object updateJobConfig(long id, ScheduleJobConfigUpdateRequest request) throws ExceptionResponse {
        ScheduleJobConfig config = findJobConfig(id);
        String errorMessage = null;

        if (request.getEnable() != null) {
            config.setEnable(request.getEnable());
        }
        if (request.getJobTimeType() != null) {
            config.setJobTimeType(request.getJobTimeType());
        }
        if (request.getJobTimeSeconds() != null) {
            config.setJobTimeSeconds(request.getJobTimeSeconds());
        }
        if (request.getLogFailEnable() != null) {
            config.setLogFailEnable(request.getLogFailEnable());
        }
        if (request.getLogCancelEnable() != null) {
            config.setLogCancelEnable(request.getLogCancelEnable());
        }
        if (request.getLogSuccessEnable() != null) {
            config.setLogSuccessEnable(request.getLogSuccessEnable());
        }
        if (request.getJobTimeCron() != null) {
            request.getJobTimeCron().validate();

            if (request.getJobTimeCron().getFullCronString() != null) {
                config.setJobTimeCron(request.getJobTimeCron().getFullCronString());
            } else if (!request.getJobTimeCron().getNoCronBuilder()) {
                JobConfigCronSupport support = request.getJobTimeCron().getSupport();
                config.setJobTimeCron(ScheduleUtils.buildCron(
                        support.getSeconds(),
                        support.getMinutes(),
                        support.getHours(),
                        support.getDaysOfWeek(),
                        support.getDaysOfMonth(),
                        support.getMonths()
                ));
            }
        }

        if (config.getJobTimeType() == JobTimeType.CRON) {
            if (config.getJobTimeCron() == null ||
                    !CronSequenceGenerator.isValidExpression(config.getJobTimeCron())) {
                errorMessage = CommonResponseMessages.NO_VALID_CRON;
            }
        } else {
            if (config.getJobTimeSeconds() == null || config.getJobTimeSeconds() < 60) {
                errorMessage = CommonResponseMessages.JOB_TIME_SECOND_INVALID;
            }
        }

        if (errorMessage != null) {
            throw new ExceptionResponse(
                    errorMessage,
                    ErrorCode.RESOURCE_NOT_FOUND
            );
        }

        config = scheduleJobConfigRepository.save(config);
        jobFactory.updateConfig(config);

        return config;
    }

    private ScheduleJobConfig findJobConfig(long id) throws ExceptionResponse {
        ScheduleJobConfig config =  scheduleJobConfigRepository.findById(id).orElse(null);
        if (config == null) {
            throw new ExceptionResponse(
                    CommonResponseMessages.JOB_CONFIG_NOT_EXIST,
                    ErrorCode.RESOURCE_NOT_FOUND
            );
        }
        return config;
    }
}
