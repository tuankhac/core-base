package com.vmo.core.schedulers;

import com.vmo.core.common.CommonResponseMessages;
import com.vmo.core.common.exception.ExceptionResponse;
import com.vmo.core.common.utils.Pair;
import com.vmo.core.configs.CommonConfig;
import com.vmo.core.configs.SchedulerConfig;
import com.vmo.core.modules.models.database.entities.shared.job.ScheduleJobConfig;
import com.vmo.core.modules.models.database.types.job.JobTimeType;
import com.vmo.core.modules.models.response.ErrorCode;
import com.vmo.core.modules.repositories.job.ScheduleJobConfigRepository;
import com.vmo.core.schedulers.annotation.Job;
import com.vmo.core.schedulers.handler.JobFailHandler;
import com.vmo.core.schedulers.handler.JobLogHandler;
import com.vmo.core.schedulers.job.JobExecutor;
import com.vmo.core.schedulers.job.ScheduledJob;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

@Component
public class JobFactory {
    private static final Logger LOG = LoggerFactory.getLogger(JobFactory.class);
    private static final ConcurrentMap<Class<? extends ScheduledJob>, Pair<JobExecutor, ScheduledFuture>> jobs = new ConcurrentHashMap();
    private static Boolean shuttingDown = false;
    private ThreadPoolTaskScheduler taskScheduler;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private SchedulerConfig schedulerConfig;
    @Autowired
    private ScheduleJobConfigRepository scheduleJobConfigRepository;
    @Autowired
    private JobLogHandler jobLogHandler;
    @Autowired
    private JobFailHandler jobFailHandler;

    public static Boolean isShuttingDown() {
        synchronized (shuttingDown) {
            return shuttingDown;
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        taskScheduler = schedulerConfig.threadPoolTaskScheduler();

        if (schedulerConfig.getEnable() == null || !schedulerConfig.getEnable()) {
            return;
        }

        Map<String, ScheduledJob> jobBeans = applicationContext.getBeansOfType(ScheduledJob.class);
        for (ScheduledJob job : jobBeans.values()) {
            loadJob(job);
        }


        //outdated config due to remove jobs from code or rename classes will be auto deleted
        if (schedulerConfig.getCleanUnused() != null && schedulerConfig.getCleanUnused()) {
            removeOldConfig();
        }
    }

    public List<ScheduleJobConfig> getRunningJobConfig() {
        return jobs.values().stream()
                .map(queue -> queue.getFirst().getJob().isRunning()
                        ? queue.getFirst().getJob().getConfig() : null
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<ScheduledJob> getInqueueJob() {
        return jobs.values().stream()
                .map(queue -> (queue.getFirst().getJob().isEnable() && !queue.getFirst().getJob().isRunning())
                        ? queue.getFirst().getJob() : null
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public ScheduledJob getJob(String jobName) {
        Class classz = findJobByName(jobName);
        if (classz == null) {
            return null;
        } else {
            return jobs.get(classz).getFirst().getJob();
        }
    }

    public ScheduledJob getJob(Class<? extends ScheduledJob> classz) {
        if (jobs.containsKey(classz)) {
            return jobs.get(classz).getFirst().getJob();
        }
        return null;
    }

    public void updateConfig(ScheduleJobConfig config) throws ExceptionResponse {
        if (config == null) return;
        Pair<JobExecutor, ScheduledFuture> queue;
        Class classz = findJobByName(config.getJobType());
        if (classz == null) return;
        else queue = jobs.get(classz);

        ScheduledJob job = queue.getFirst().getJob();

        ScheduleJobConfig oldConfig = job.getConfig();
        job.setConfig(config);
        if (config.isEnable()) {
            if (oldConfig.isEnable()) {
                job.buildTrigger();
                if (!job.isRunning()) {
                    requeue(classz);
                }
            } else {
                jobs.get(classz).setSecond(enqueue(job));
            }
        } else {
            if (!cancel(job.getClass(), false)) {
                throw new ExceptionResponse(
                        CommonResponseMessages.DEQUEUE_JOB_FAIL,
                        ErrorCode.UNCATEGORIZED_SERVER_ERROR
                );
            }
        }
    }

    public void shutDown() {
        synchronized (shuttingDown) {
            shuttingDown = true;

            for (Pair<JobExecutor, ScheduledFuture> queue : jobs.values()) {
                ScheduledJob job = queue.getFirst().getJob();
                ScheduledFuture future = queue.getSecond();

                if (future != null && !(future.isCancelled() || future.isDone()) && !job.isRunning()) {
//                    (job.getConfig().getJobTimeType() == JobTimeType.FIX_RATE || !job.isRunning())) {
                    queue.getFirst().forcedCancel();
                    boolean cancelSuccess = future.cancel(false);
                    if (cancelSuccess) {
                        jobLogHandler.logJobCancel(job);
                    }
                    if (job.isRunning()) {
                        System.out.println(queue.getSecond().getClass().getName());
                        System.out.println(queue.getSecond().getClass().getSimpleName() + " is running");

                        try {
                            future.get();
                        } catch (InterruptedException | ExecutionException e) {
                            jobFailHandler.handleError(e);
                        }
                    }
                }

            }
        }
    }

    public boolean isRunning(Class<? extends ScheduledJob> classz) {
        return jobs.containsKey(classz) && jobs.get(classz).getFirst().getJob().isRunning();
    }

    public boolean isRunning(String jobName) {
        Class classz = findJobByName(jobName);
        return classz != null && jobs.get(classz).getFirst().getJob().isRunning();
    }

    public boolean updateEnable(ScheduledJob job, boolean isEnable) {
        if (!jobs.containsKey(job.getClass())) return false;
        Pair<JobExecutor, ScheduledFuture> queue = jobs.get(job.getClass());
        ScheduledFuture future = queue.getSecond();
        if (isEnable != job.getConfig().isEnable()) { //must diff with old config
            if (isEnable) {
                jobs.get(job.getClass()).setSecond(enqueue(job));
            } else {
                queue.getFirst().forcedCancel();
                boolean cancelSuccess = future.cancel(false);
                if (cancelSuccess) {
                    jobLogHandler.logJobCancel(job);
                }
                return cancelSuccess;
            }
        }
        return true;
    }

    public void forceRun(Class<? extends ScheduledJob> clazz) {
        if (jobs.containsKey(clazz)) {
            ScheduledJob job = jobs.get(clazz).getFirst().getJob();
            if (!job.isRunning()) {
                if (cancel(clazz, false)) {
                    jobs.get(clazz).setSecond(enqueue(job, 0, true));
                }
            }
        }
    }

    private void loadJob(ScheduledJob job) {
        Class classz = findJobByName(job.getClass().getSimpleName());
        if ((classz != null && jobs.containsKey(classz)) || jobs.containsKey(job.getClass())) {
            throw new RuntimeException(CommonResponseMessages.DUPLICATE_JOB);
        }

        Job setting = job.getClass().getAnnotation(Job.class);
        ScheduleJobConfig config = scheduleJobConfigRepository.findOne(job.getClass().getSimpleName());
        if (config == null) {
            config = new ScheduleJobConfig();
            //default
            config.setService(commonConfig.getService());
            config.setJobType(job.getClass().getSimpleName());
            config.setJobTimeType(JobTimeType.FIX_DELAY);
            config.setLogFailEnable(true);
            //end default

            if (setting != null) {
                config.setEnable(setting.preEnable());
                config.setLogFailEnable(setting.defaultLogFail());

                if (setting.defaultTimeType() != null) {
                    config.setJobTimeType(setting.defaultTimeType());
                }
                if (!StringUtils.isBlank(setting.defaultCron())
                        && CronSequenceGenerator.isValidExpression(setting.defaultCron())
                ) {
                    config.setJobTimeCron(setting.defaultCron());
                }
                if (setting.defaultTimeSeconds() > 0) {
                    config.setJobTimeSeconds(setting.defaultTimeSeconds());
                }
            }
            config = scheduleJobConfigRepository.save(config);
        }
        job.setConfig(config);

        Pair<JobExecutor, ScheduledFuture> scheduler = new Pair();
        JobExecutor executor = new JobExecutor(jobLogHandler, job);
        scheduler.setFirst(executor);
        jobs.put(job.getClass(), scheduler);

        if ((config.isEnable() || (setting != null && setting.overrideEnable()))
                && (setting == null || !setting.forceDisable())
        ) {
            if (schedulerConfig.getFirstRunDelay() != null && schedulerConfig.getFirstRunDelay() > 0) {
                scheduler.setSecond(enqueue(job, schedulerConfig.getFirstRunDelay(), false));
            } else {
                scheduler.setSecond(enqueue(job));
            }
        }
    }

    private void requeue(Class<? extends ScheduledJob> classz) throws ExceptionResponse {
        if (!jobs.containsKey(classz)) return;
        if (!cancel(classz, false)) {
            throw new ExceptionResponse(
                    CommonResponseMessages.DEQUEUE_JOB_FAIL,
                    ErrorCode.UNCATEGORIZED_SERVER_ERROR
            );
        }
        jobs.get(classz).setSecond(enqueue(jobs.get(classz).getFirst().getJob()));
    }

    private ScheduledFuture enqueue(ScheduledJob job) {
        return enqueue(job, 0, false);
    }

    private ScheduledFuture enqueue(ScheduledJob job, int firstRunDelay, boolean forced) {
        if (!jobs.containsKey(job.getClass())) {
            return null;
        }

        JobExecutor executor = jobs.get(job.getClass()).getFirst();
        job.buildTrigger(firstRunDelay, forced);

        return taskScheduler.schedule(executor, job);
    }

    private boolean cancel(Class<? extends ScheduledJob> classz, boolean force) {
        if (!jobs.containsKey(classz)) return false;
        Pair<JobExecutor, ScheduledFuture> queue = jobs.get(classz);
        ScheduledFuture future = queue.getSecond();
        if (future == null || future.isCancelled() || future.isDone()) return true;

        return future.cancel(force);
    }

    private void removeOldConfig() {
        List<ScheduleJobConfig> configs = scheduleJobConfigRepository.findAll();
        if (configs.size() > jobs.size()) {
            List<ScheduleJobConfig> outdated = configs.stream()
                    .filter(config -> {
                        return getJob(config.getJobType()) == null;
                    })
                    .peek(config -> config.setDelete(true))
                    .collect(Collectors.toList());
            if (outdated.size() > 0) {
                scheduleJobConfigRepository.saveAll(outdated);
            }
        }
    }

    private Class<? extends ScheduledJob> findJobByName(String jobName) {
        for (Class<? extends ScheduledJob> classz : jobs.keySet()) {
            if (classz.getSimpleName().equals(jobName)) {
                return classz;
            }
        }
        return null;
    }
}
