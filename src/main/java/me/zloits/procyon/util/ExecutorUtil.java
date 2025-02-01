package me.zloits.procyon.util;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import me.zloits.procyon.exception.ProcyonException;

import java.util.concurrent.*;

@UtilityClass
public class ExecutorUtil {

    @Getter
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    @Getter
    private final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(10);

    public <T> T execute(boolean async, Callable<T> future) {
        if (async) {
            Future<T> future1 = executorService.submit(future);

            try {
                return future1.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            try {
                return future.call();
            } catch (Exception e) {
                throw new ProcyonException(e.getMessage());
            }
        }
    }

    public void execute(boolean async, Runnable runnable) {
        if (async) {
            executorService.execute(runnable);
        } else {
            runnable.run();
        }
    }
}
