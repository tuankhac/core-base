package com.vmo.core.configs;

import com.vmo.core.Constants;
import com.vmo.core.configs.security.SecurityConfig;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

@Configuration
@EntityScan(Constants.Links.MODELS_PACKAGE)
@EnableJpaRepositories({Constants.Links.LOGGING_PACKAGE, Constants.Links.REPOSITORIES_PACKAGE})
//, bootstrapMode = BootstrapMode.DEFERRED) //Constants.Links.LOGGING_PACKAGE
public class CommonDataSourceConfig {
    public static final String SCHEMA_DBO = "dbo";
    @Getter
    private static String secretKey;
    @Autowired
    private SecurityConfig securityConfig;

    public static SessionFactory getSessionFactory(EntityManagerFactory entityManagerFactory) {
        if (entityManagerFactory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }
        return entityManagerFactory.unwrap(SessionFactory.class);
    }

    @PostConstruct
    private void init() {
        secretKey = securityConfig.getSecretKey();
    }
}
