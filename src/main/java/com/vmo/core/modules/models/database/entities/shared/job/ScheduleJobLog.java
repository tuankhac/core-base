package com.vmo.core.modules.models.database.entities.shared.job;

import com.vmo.core.Constants;
import lombok.Data;
import org.joda.time.LocalDateTime;

import javax.persistence.*;

@Data
@Entity
@Table(schema = Constants.SCHEMA_DBO)
public class ScheduleJobLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long jobId;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private LocalDateTime cancelTime;
    private LocalDateTime failTime;
    private String stackTrace;
    private String customLogString;
    private Float customLogNumber;
}
