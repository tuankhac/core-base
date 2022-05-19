package com.vmo.core.modules.models.database.entities.shared.log;

import lombok.Data;
import org.joda.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class LogAsyncTaskError {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String service;
    private String method;
    private String exceptionName;
    private String stacktrace;
//    @CreatedDate
//    @Generated(GenerationTime.INSERT)
    private LocalDateTime createdTime = LocalDateTime.now();
}
