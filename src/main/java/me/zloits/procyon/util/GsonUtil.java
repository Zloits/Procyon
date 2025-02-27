package me.zloits.procyon.util;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * Utility class for JSON serialization and deserialization using a shared Gson instance.
 * This prevents redundant Gson object creation.
 */
@UtilityClass
public class GsonUtil {

    @Getter
    private final Gson gson = new Gson();

    public String toJson(@NonNull Object object) {
        return gson.toJson(object);
    }

    public <T> T fromJson(@NonNull String json, @NonNull Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
