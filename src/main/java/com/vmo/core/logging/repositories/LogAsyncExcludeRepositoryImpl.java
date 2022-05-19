package com.vmo.core.logging.repositories;

import com.vmo.core.common.utils.CommonUtils;
import com.vmo.core.common.utils.ConverterSqlUtils;
import com.vmo.core.configs.CommonConfig;
import com.vmo.core.configs.CommonDataSourceConfig;
import com.vmo.core.modules.models.database.entities.shared.log.LogAsyncExclude;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
public class LogAsyncExcludeRepositoryImpl implements LogAsyncExcludeRepositoryExtended {
    private final SessionFactory sessionFactory;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    @Lazy
    private LogAsyncExcludeRepository logAsyncExcludeRepository;

    public LogAsyncExcludeRepositoryImpl(EntityManagerFactory entityManagerFactory) {
        sessionFactory = CommonDataSourceConfig.getSessionFactory(entityManagerFactory);
    }

    @Override
//    @Transactional(readOnly = true)
    public List<LogAsyncExclude> findAllExcludedLogs() {
        String query = "SELECT * " +
                "FROM log_async_exclude " +
                "WHERE is_excluded = 1 " +
                "AND (method IS NOT NULL OR exception_name IS NOT NULL) " +
                "AND service = " + ConverterSqlUtils.convertStringToStringQuery(commonConfig.getService());

        Session session = sessionFactory.openSession();
        return CommonUtils.safeRunQuery(
                () -> session.createNativeQuery(query)
                        .addEntity(LogAsyncExclude.class)
                        .list(),
                session
        );
    }
}
