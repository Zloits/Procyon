package me.zloits.procyon.util;

import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link InstanceRegistry} provides a way to manage instances that cannot be obtained by default.
 * It allows developers to store initialized instances and retrieve them later,
 * avoiding the need to write custom getInstance() methods for otherwise inaccessible instances.
 */
@UtilityClass
public class InstanceRegistry {

    @Getter
    private final Map<Class<?>, Object> instanceMap = new ConcurrentHashMap<>();

    /**
     * Adds an instance to the storage if an instance of the same type doesn't already exist.
     *
     * @param instance The instance to add.
     * @param <T> The type of the instance.
     * @return {@code true} if the instance was added successfully, {@code false} if an instance
     *         of the same type already exists.
     */
    public <T> boolean add(T instance) {
        return instanceMap.putIfAbsent(instance.getClass(), instance) == null;
    }

    /**
     * Retrieves an instance of the specified class type.
     *
     * @param instanceClass The class type of the instance to retrieve.
     * @param <T> The type of the instance.
     * @return An {@link Optional} containing the stored instance if found, otherwise an empty {@link Optional}.
     */
    public <T> Optional<T> get(Class<T> instanceClass) {
        return Optional.ofNullable(instanceClass.cast(instanceMap.get(instanceClass)));
    }
}