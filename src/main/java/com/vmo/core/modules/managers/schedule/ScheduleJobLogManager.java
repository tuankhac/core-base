package com.vmo.core.modules.managers.schedule;

import com.vmo.core.modules.models.database.entities.shared.job.ScheduleJobLog;
import com.vmo.core.modules.models.response.ListObjResponse;
import com.vmo.core.modules.models.response.MessageResponse;
import com.vmo.core.modules.repositories.job.ScheduleJobLogRepository;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class ScheduleJobLogManager {
    @Autowired
    private ScheduleJobLogRepository scheduleJobLogRepository;

    public Object getLogs(Long jobId, Pageable pageable) {
        ListObjResponse<ScheduleJobLog> response = new ListObjResponse<>(pageable.getPageNumber(), pageable.getPageSize());
        response.setPageSize(pageable.getPageSize());
        response.setTotalItem(scheduleJobLogRepository.countAll(jobId));
        if (response.getTotalItem() > 0) {
            response.setData(scheduleJobLogRepository.findAll(
                    jobId, pageable.getOffset(), pageable.getPageSize()
            ));
        }
        return response;
    }

    public Object purgeLog(Long jobId, LocalDateTime olderThan) {
        int deleteCount = scheduleJobLogRepository.purgeLogs(jobId, new Timestamp(olderThan.toDateTime().getMillis()));
        return new MessageResponse("Clean " + String.valueOf(deleteCount) + " log(s)");
    }
}
