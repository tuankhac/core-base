package com.vmo.core.logging;

import com.vmo.core.common.utils.Single;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.support.Repositories;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

//Multi-thread safe saving log to database and be aware of high db connection usage
public abstract class BaseLogger<T> {
    private final Logger LOG;
    private final List<T> logs = new ArrayList<>();
    private final Single<Boolean> isSaving = new Single<>(false);

    //    @Autowired
//    private ApplicationContext applicationContext;
    private final Repositories repositories;
    private HikariPool hikariPool;

    public BaseLogger(ApplicationContext applicationContext, HikariDataSource hikariDataSource) {
        LOG = LoggerFactory.getLogger(getClass());
        repositories = new Repositories(applicationContext);
        if (hikariDataSource != null) {
            hikariPool = (HikariPool) new DirectFieldAccessor(hikariDataSource).getPropertyValue("pool");
        }
    }

    public abstract void reloadConfig();

    protected void addLog(T t) {
//        LOG.info("=== Attempt to add log - " + Thread.currentThread().getName());
        synchronized (isSaving) {
            logs.add(t);
        }
        if (!isSaving.getValue()) {
            saveLogs();
        }
    }

    private void saveLogs() {
        isSaving.setValue(true);

//        LOG.info("=== Attempt to save - " + Thread.currentThread().getName());

        synchronized (isSaving) {
            try {
//                LOG.info("=== Saving - " + Thread.currentThread().getName());
                if (logs.isEmpty()) {
                    isSaving.setValue(false);
                    return;
                }

                if (hikariPool != null) {
                    while (hikariPool.getIdleConnections() / (float) hikariPool.getTotalConnections() < 0.1) {
                        try {
                            hikariPool.wait(5000);
                        } catch (Exception e) {
                            LOG.error(e.getMessage());
                            // Restore interrupted state...
                            Thread.currentThread().interrupt();
                        }
                    }

                    while (hikariPool.getIdleConnections() / (float) hikariPool.getTotalConnections() < 0.2) {
                        try {
                            hikariPool.wait(2000);
                        } catch (Exception e) {
                            LOG.error(e.getMessage());
                            // Restore interrupted state...
                            Thread.currentThread().interrupt();
                        }
                    }
                }

                Class loggerClass = getClass();

                while (loggerClass != null && !BaseLogger.class.equals(loggerClass.getSuperclass())) {
                    loggerClass = loggerClass.getSuperclass();
                }
                if (loggerClass == null) {
                    LOG.error("Can not get log model entity");
                    return;
                }

                Class logClass = (Class) ((ParameterizedType) loggerClass.getGenericSuperclass()).getActualTypeArguments()[0];

                JpaRepository repository = (JpaRepository) repositories.getRepositoryFor(logClass).orElse(null);
                if (repository == null) {
                    LOG.error("Can not find repository for logger: " + loggerClass.getSimpleName());
                    return;
                }

                List<T> savingLogs = new ArrayList<>();
                savingLogs.addAll(logs);
                for (T log : savingLogs) {
                    repository.save(log);
                    logs.remove(log);
                }
            } finally {
                isSaving.setValue(false);
            }
        }
    }
}
