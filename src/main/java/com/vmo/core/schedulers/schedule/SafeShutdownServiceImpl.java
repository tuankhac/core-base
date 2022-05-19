package com.vmo.core.schedulers.schedule;

import com.vmo.core.modules.managers.schedule.SafeShutdownManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SafeShutdownServiceImpl implements SafeShutdownService {
    @Autowired
    private SafeShutdownManager manager;

    public Object safeShutdown() {
        return manager.safeShutdown();
    }
}
