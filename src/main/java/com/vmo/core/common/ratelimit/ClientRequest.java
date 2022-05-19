package com.vmo.core.common.ratelimit;

import lombok.Data;
import org.joda.time.LocalDateTime;

@Data
public class ClientRequest {
    private Object apiHandler;
    private LocalDateTime startCycleTime;
    private long count;
}
