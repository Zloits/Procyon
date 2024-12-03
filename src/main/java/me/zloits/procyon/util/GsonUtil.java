package me.zloits.procyon.util;

import com.google.gson.Gson;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GsonUtil {

    private final Gson gson = new Gson();

    public String toJson(@NonNull Object object) {
        return gson.toJson(object);
    }

    public <T> T fromJson(@NonNull String json, @NonNull Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
