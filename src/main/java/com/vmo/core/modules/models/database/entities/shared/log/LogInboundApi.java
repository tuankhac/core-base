package com.vmo.core.modules.models.database.entities.shared.log;

import lombok.Data;
import org.joda.time.LocalDateTime;
import org.springframework.http.HttpMethod;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class LogInboundApi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String service;
    private String path;
    private HttpMethod httpMethod;
    private String requestQuery;
    private String requestBody;
    private Integer responseHttpStatusCode;
    private String responseBody;
//    @CreatedDate
//    @Generated(GenerationTime.INSERT)
    private LocalDateTime createdTime = LocalDateTime.now();
}
