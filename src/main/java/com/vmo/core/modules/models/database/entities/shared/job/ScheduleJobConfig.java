package com.vmo.core.modules.models.database.entities.shared.job;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vmo.core.Constants;
import com.vmo.core.modules.models.database.types.job.JobTimeType;
import lombok.Data;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.joda.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;

@Data
@Entity
@Table(schema = Constants.SCHEMA_DBO)
public class ScheduleJobConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String jobType;
    private boolean isEnable;
    private JobTimeType jobTimeType;
    private Long jobTimeSeconds;
    private String jobTimeCron;
    private Boolean logFailEnable;
    private Boolean logCancelEnable;
    private Boolean logSuccessEnable;
    @CreatedDate
    @Generated(value = GenerationTime.INSERT)
    private LocalDateTime createdTime;
    @LastModifiedDate
    @Generated(GenerationTime.ALWAYS)
    private LocalDateTime lastUpdate;
    private String service;
    @JsonIgnore
    private boolean isDelete;
}
