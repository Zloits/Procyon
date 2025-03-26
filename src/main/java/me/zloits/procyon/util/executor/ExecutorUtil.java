package me.zloits.procyon.util.executor;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import me.zloits.procyon.exception.ProcyonException;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * {@link ExecutorUtil} provides utility methods for executing tasks asynchronously or synchronously.
 * It manages a fixed-thread pool and a scheduled thread pool to optimize task execution.
 */
@UtilityClass
public class ExecutorUtil {

    @Getter
    private final Map<String, ExecutorService> pools = new ConcurrentHashMap<>();
    @Getter
    private final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(10);

    /**
     * Store pool to Map.
     */
    public ExecutorService createPool(@NonNull SimplePoolConfiguration poolConfiguration, @NonNull ExecutorType executorType) {
        if (getPool(poolConfiguration.getPoolName()).isPresent()) {
            throw new IllegalStateException("Unable to create pool name: " + poolConfiguration.getPoolName() + " because pool with exact name is already exists.");
        }

        ExecutorService executorService = null;
        switch (executorType) {
            case SINGLE -> {
                executorService = Executors.newSingleThreadExecutor(poolConfiguration.getThreadFactory());
                break;
            }
            case FIXED -> {
                executorService = Executors.newFixedThreadPool(poolConfiguration.getNThreads(), poolConfiguration.getThreadFactory());
                break;
            }
            case CACHED -> {
                executorService = Executors.newCachedThreadPool(poolConfiguration.getThreadFactory());
            }
        }

        pools.put(poolConfiguration.getPoolName(), executorService);

        return executorService;
    }

    /**
     * Executes a {@link Callable} task, either asynchronously or synchronously with returnable.
     *
     * @param async If {@code true}, the task runs asynchronously using the thread pool.
     *              Otherwise, it runs synchronously on the current thread.
     * @param future The {@link Callable} task to execute.
     * @param <T> The return type of the callable task.
     * @return The result of the task execution.
     * @throws RuntimeException If an exception occurs during async execution.
     * @throws ProcyonException If an exception occurs during sync execution.
     */
    public <T> T execute(boolean async, ExecutorService executorService, Callable<T> future) {
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

    /**
     * Executes a {@link Runnable} task, either asynchronously or synchronously with non-returnable / void only.
     *
     * @param async If {@code true}, the task runs asynchronously using the thread pool.
     *              Otherwise, it runs synchronously on the current thread.
     * @param runnable The {@link Runnable} task to execute.
     */
    public void execute(boolean async, ExecutorService executorService, Runnable runnable) {
        if (async) {
            executorService.execute(runnable);
        } else {
            runnable.run();
        }
    }

    /**
     * Checks if the current thread belongs to the thread pool.
     *
     * @return {@code true} if the current thread is part of the executor pool, {@code false} otherwise.
     */
    public boolean isPool() {
        return Thread.currentThread().getName().startsWith("pool");
    }

    /**
     * Checks if the current thread is not part of the executor pool.
     *
     * @return {@code true} if the current thread is not in the executor pool, {@code false} otherwise.
     */
    public boolean getOtherwise() {
        return !isPool();
    }

    /**
     * Retrieve {@link ExecutorService} from "pool name" from {@link SimplePoolConfiguration}.
     *
     * @param poolName Pool name.
     * @return An {@link Optional} containing the stored {@link ExecutorService} if found, otherwise an empty {@link Optional}.
     */
    public Optional<ExecutorService> getPool(@NonNull String poolName) {
        return Optional.ofNullable(getPools().get(poolName));
    }
}
