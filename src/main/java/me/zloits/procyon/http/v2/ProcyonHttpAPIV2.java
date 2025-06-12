package me.zloits.procyon.http.v2;

import lombok.Builder;
import lombok.NonNull;
import lombok.Setter;
import me.zloits.procyon.http.v1.ProcyonHttpResponse;
import me.zloits.procyon.util.GsonUtil;
import me.zloits.procyon.util.executor.ExecutorType;
import me.zloits.procyon.util.executor.ExecutorUtil;
import me.zloits.procyon.util.executor.PoolConfiguration;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Builder
public class ProcyonHttpAPIV2 implements ProcyonHttpAPI {

    private final ExecutorService executorService = ExecutorUtil.createPool(
            new PoolConfiguration("http-pool-v2", Executors.defaultThreadFactory(), 4),
            ExecutorType.FIXED
    );

    private final CloseableHttpClient httpClient;

    @Setter
    private String apiKeyHeader;
    @Setter
    private String apiKey;
    @Setter
    private String URL = "https://example.com";

    @Override
    public @NonNull String getApiKeyHeader() {
        return apiKeyHeader;
    }

    @Override
    public @NonNull String getApiKey() {
        return apiKey;
    }

    @Override
    public boolean isUseApiKey() {
        return !apiKey.isEmpty() && !apiKeyHeader.isEmpty();
    }

    @Override
    public <T> ProcyonHttpResponse<T> get(@NonNull String url, @NonNull Class<T> clazz) {
        String httpUrl = URL + url;

        HttpGet httpGet = new HttpGet(httpUrl);
        if (isUseApiKey()) {
            httpGet.addHeader(apiKeyHeader, apiKey);
        }

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            String json = EntityUtils.toString(response.getEntity());

            return new ProcyonHttpResponse<>(response, GsonUtil.fromJson(json, clazz));
        } catch (IOException | ParseException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
