package me.zloits.procyon.http.v2;

import lombok.NonNull;
import me.zloits.procyon.http.v1.ProcyonHttpResponse;

public interface ProcyonHttpAPI {

    @NonNull
    String getApiKeyHeader();

    @NonNull
    String getApiKey();

    boolean isUseApiKey();

    <T> ProcyonHttpResponse<T> get(@NonNull String url, @NonNull Class<T> clazz);
}
