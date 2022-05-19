package com.vmo.core.schedulers.handler;

import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

@Component
public class JobFailHandler implements ErrorHandler {
    @Override
    public void handleError(Throwable throwable) {
        throwable.printStackTrace();
    }
}
