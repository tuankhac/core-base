package com.vmo.core.logging.repositories;

import com.vmo.core.common.utils.CommonUtils;
import com.vmo.core.common.utils.ConverterSqlUtils;
import com.vmo.core.configs.CommonConfig;
import com.vmo.core.configs.CommonDataSourceConfig;
import com.vmo.core.modules.models.database.entities.shared.log.LogApiConfig;
import com.vmo.core.modules.models.database.types.log.ApiCallSource;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
//@Transactional
public class LogApiConfigRepositoryImpl implements LogApiConfigRepositoryExtended {
    private final SessionFactory sessionFactory;
    //    @PersistenceContext
//    private EntityManager entityManager;
    @Autowired
    private CommonConfig commonConfig;

    public LogApiConfigRepositoryImpl(EntityManagerFactory entityManagerFactory) {
        sessionFactory = CommonDataSourceConfig.getSessionFactory(entityManagerFactory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LogApiConfig> findAllActive(ApiCallSource apiCallSource) {
        String query = "SELECT * " +
                "FROM log_api_config " +
                "WHERE is_enable = 1 " +
                "AND (:source IS NULL OR source = :source) " +
                "AND service = " + ConverterSqlUtils.convertStringToStringQuery(commonConfig.getService());

        Session session = sessionFactory.openSession();
        return CommonUtils.safeRunQuery(
                () -> session.createNativeQuery(query)
                        .setParameter("source", apiCallSource != null ? apiCallSource.getValue() : null)
                        .addEntity(LogApiConfig.class)
                        .list(),
                session
        );

    }
}
