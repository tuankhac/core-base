package com.vmo.core.logging.asynctask;

import org.springframework.core.task.AsyncTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Deprecated
public class AsyncTaskFactory implements AsyncTaskExecutor {
    private final AsyncTaskExecutor executor;
    private AsyncTaskLogger asyncTaskLogger;

    public AsyncTaskFactory(AsyncTaskExecutor executor, AsyncTaskLogger asyncTaskLogger) {
        this.executor = executor;
    }

    @Override
    public void execute(Runnable task) {
        executor.execute(createWrappedRunnable(task));
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        executor.execute(createWrappedRunnable(task), startTimeout);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return executor.submit(createWrappedRunnable(task));
    }

    @Override
    public <T> Future<T> submit(final Callable<T> task) {
        return executor.submit(createCallable(task));
    }

    private <T> Callable<T> createCallable(final Callable<T> task) {
        return () -> {
            try {
                return task.call();
            } catch (Exception e) {
                handleError(e);
                throw e;
            }
        };
    }

    private Runnable createWrappedRunnable(final Runnable task) {
        return () -> {
            try {
                task.run();
            } catch (Exception e) {
                handleError(e);
            }
        };
    }

    private void handleError(Exception e) {
        System.out.println("CAUGHT!");
    }
}
