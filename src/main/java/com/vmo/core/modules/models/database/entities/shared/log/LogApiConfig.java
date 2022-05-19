package com.vmo.core.modules.models.database.entities.shared.log;

import com.vmo.core.modules.models.database.types.log.ApiCallSource;
import lombok.Data;
import org.springframework.http.HttpMethod;

import javax.persistence.*;

@Entity
@Data
public class LogApiConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String service;
    private ApiCallSource source;
    private String url;
    @Enumerated(EnumType.STRING)
    private HttpMethod httpMethod;
    private boolean isEnable;
 }
