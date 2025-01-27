package me.zloits.procyon.util;

import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class InstanceGetter {

    @Getter
    private final List<Object> instances = new ArrayList<>();

    public <T> boolean add(T instance) {
        if (get(instance.getClass()) != null) {
            return false;
        }
        return instances.add(instance);
    }

    public <T> T get(Class<T> instanceClass) {
        for (Object object : instances) {
            if (instanceClass.isInstance(object)) {
                return instanceClass.cast(object);
            }
        } return null;
    }
}
