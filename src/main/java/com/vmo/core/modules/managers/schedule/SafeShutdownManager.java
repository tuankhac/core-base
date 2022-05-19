package com.vmo.core.modules.managers.schedule;

import com.vmo.core.common.CommonResponseMessages;
import com.vmo.core.modules.models.response.MessageResponse;
import com.vmo.core.schedulers.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SafeShutdownManager {
    @Autowired
    private JobFactory jobFactory;

    public Object safeShutdown() {
        jobFactory.shutDown();
        return new MessageResponse(CommonResponseMessages.SAFE_SHUTDOWN_SUCCESS);
    }
}
