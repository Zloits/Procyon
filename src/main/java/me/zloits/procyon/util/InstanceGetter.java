package me.zloits.procyon.util;

import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link InstanceGetter} provides a way to manage instances that cannot be obtained by default.
 * It allows developers to store initialized instances and retrieve them later,
 * avoiding the need to write custom getInstance() methods for otherwise inaccessible instances.
 */
@UtilityClass
public class InstanceGetter {

    @Getter
    private final List<Object> instances = new ArrayList<>();

    /**
     * Adds an instance to the storage if an instance of the same type doesn't already exist.
     *
     * @param instance The instance to add.
     * @param <T> The type of the instance.
     * @return {@code true} if the instance was added successfully, {@code false} if an instance
     *         of the same type already exists.
     */
    public <T> boolean add(T instance) {
        if (get(instance.getClass()) != null) {
            return false;
        }
        return instances.add(instance);
    }

    /**
     * Retrieves an instance of the specified class type.
     *
     * @param instanceClass The class type of the instance to retrieve.
     * @param <T> The type of the instance.
     * @return The stored instance of the specified type.
     * @throws IllegalStateException If multiple instances of the same type exist or none are found.
     */
    public <T> T get(Class<T> instanceClass) {
        List<T> matchedInstances = new ArrayList<>();

        for (Object object : instances) {
            if (instanceClass.isInstance(object)) {
                matchedInstances.add(instanceClass.cast(object));
            }
        }

        if (matchedInstances.isEmpty()) return null;
        if (matchedInstances.size() != 1) throw new IllegalStateException("Expected to obtain a single instance of "
                    + instanceClass.getName() + ", but found " + matchedInstances.size() + ".");

        return matchedInstances.getFirst();
    }
}
