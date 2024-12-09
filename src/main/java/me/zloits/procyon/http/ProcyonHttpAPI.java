package me.zloits.procyon.http;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.zloits.procyon.Procyon;
import me.zloits.procyon.http.exception.ProcyonHttpException;
import me.zloits.procyon.util.GsonUtil;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Getter
public class ProcyonHttpAPI {

    @Getter
    private static ProcyonHttpAPI httpAPI;

    private final Procyon procyon = Procyon.getProcyon();
    private final CloseableHttpClient httpClient;

    @Setter
    private String BASE_URL = "https://api.example.com";

    public ProcyonHttpAPI(CloseableHttpClient httpClient) {
        httpAPI = this;

        this.httpClient = httpClient;
    }

    @SneakyThrows
    public <T> ProcyonHttpResponse<T> GET(String url, String apiKey, Class<T> clazz) {
        CompletableFuture<ProcyonHttpResponse<T>> completableFuture = CompletableFuture.supplyAsync(() -> {
            String httpUrl = BASE_URL + url;

            HttpGet httpGet = new HttpGet(httpUrl);
            httpGet.addHeader("X_API_KEY", apiKey);

            try {
                CloseableHttpResponse response = httpClient.execute(httpGet);

                String json = EntityUtils.toString(response.getEntity());

                return new ProcyonHttpResponse<>(response, GsonUtil.fromJson(json, clazz));
            } catch (IOException | ParseException e) {
                e.printStackTrace();
                return null;
            }
        }, procyon.getExecutorService());

        return completableFuture.get();
    }

    @SneakyThrows
    public <T> ProcyonHttpResponse<T> GET(String url, Class<T> clazz) {
        CompletableFuture<ProcyonHttpResponse<T>> completableFuture = CompletableFuture.supplyAsync(() -> {
            String httpUrl = BASE_URL + url;

            try {
                CloseableHttpResponse response = httpClient.execute(new HttpGet(httpUrl));

                String json = EntityUtils.toString(response.getEntity());

                return new ProcyonHttpResponse<T>(response, GsonUtil.fromJson(json, clazz));
            } catch (IOException | ParseException e) {
                e.printStackTrace();
                return null;
            }
        }, procyon.getExecutorService());

        return completableFuture.get();
    }

    @SneakyThrows
    public ProcyonHttpResponse<Object> POST(String url, String apiKey, Object object) {
        CompletableFuture<ProcyonHttpResponse<Object>> completableFuture = CompletableFuture.supplyAsync(() -> {
            String httpUrl = BASE_URL + url;

            HttpPost httpPost = new HttpPost(httpUrl);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("X_API_KEY", apiKey);

            String json = GsonUtil.toJson(object);
            StringEntity stringEntity = new StringEntity(json);
            httpPost.setEntity(stringEntity);

            try {
                CloseableHttpResponse response = httpClient.execute(httpPost);

                ProcyonHttpResponse<Object> procyonHttpResponse = new ProcyonHttpResponse<>(response, object);
                if (procyonHttpResponse.response().getCode() != 200) throw new ProcyonHttpException(procyonHttpResponse.response().getReasonPhrase());

                return procyonHttpResponse;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, procyon.getExecutorService());

        return completableFuture.get();
    }

    @SneakyThrows
    public ProcyonHttpResponse<Object> POST(String url, Object object) {
        CompletableFuture<ProcyonHttpResponse<Object>> completableFuture = CompletableFuture.supplyAsync(() -> {
            String httpUrl = BASE_URL + url;

            HttpPost httpPost = new HttpPost(httpUrl);
            String json = GsonUtil.toJson(object);
            StringEntity stringEntity = new StringEntity(json);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.setEntity(stringEntity);

            try {
                CloseableHttpResponse response = httpClient.execute(httpPost);

                ProcyonHttpResponse<Object> procyonHttpResponse = new ProcyonHttpResponse<>(response, object);
                if (procyonHttpResponse.response().getCode() != 200) throw new ProcyonHttpException(procyonHttpResponse.response().getReasonPhrase());

                return procyonHttpResponse;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, procyon.getExecutorService());

        return completableFuture.get();
    }

    @SneakyThrows
    public ProcyonHttpResponse<Object> PUT(String url, String apiKey, Object object) {
        CompletableFuture<ProcyonHttpResponse<Object>> completableFuture = CompletableFuture.supplyAsync(() -> {
            String httpUrl = BASE_URL + url;

            HttpPut httpPut = new HttpPut(httpUrl);
            httpPut.addHeader("Content-Type", "application/json");
            httpPut.addHeader("X_API_KEY", apiKey);

            String json = GsonUtil.toJson(object);
            StringEntity stringEntity = new StringEntity(json);
            httpPut.setEntity(stringEntity);

            try {
                CloseableHttpResponse response = httpClient.execute(httpPut);

                ProcyonHttpResponse<Object> procyonHttpResponse = new ProcyonHttpResponse<>(response, object);
                if (procyonHttpResponse.response().getCode() != 200) throw new ProcyonHttpException(procyonHttpResponse.response().getReasonPhrase());

                return procyonHttpResponse;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, procyon.getExecutorService());

        return completableFuture.get();
    }

    @SneakyThrows
    public ProcyonHttpResponse<Object> PUT(String url, Object object) {
        CompletableFuture<ProcyonHttpResponse<Object>> completableFuture = CompletableFuture.supplyAsync(() -> {
            String httpUrl = BASE_URL + url;

            HttpPut httpPut = new HttpPut(httpUrl);
            String json = GsonUtil.toJson(object);
            StringEntity stringEntity = new StringEntity(json);
            httpPut.addHeader("Content-Type", "application/json");
            httpPut.setEntity(stringEntity);

            try {
                CloseableHttpResponse response = httpClient.execute(httpPut);

                ProcyonHttpResponse<Object> nithiumHttpResponse = new ProcyonHttpResponse<>(response, object);
                if (nithiumHttpResponse.response().getCode() != 200) throw new ProcyonHttpException(nithiumHttpResponse.response().getReasonPhrase());

                return nithiumHttpResponse;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, procyon.getExecutorService());

        return completableFuture.get();
    }

    @SneakyThrows
    public boolean DELETE(String url, String apiKey) throws ProcyonHttpException {
        CompletableFuture<Boolean> completableFuture = CompletableFuture.supplyAsync(() -> {
            String httpUrl = BASE_URL + url;

            HttpDelete httpDelete = new HttpDelete(httpUrl);
            httpDelete.setHeader("Accept", "application/json");
            httpDelete.addHeader("X_API_KEY", apiKey);

            try {
                CloseableHttpResponse response = httpClient.execute(httpDelete);

                if (response.getCode() != 200) throw new ProcyonHttpException(response.getReasonPhrase());

                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }, procyon.getExecutorService());

        return completableFuture.get();
    }

    @SneakyThrows
    public boolean DELETE(String url) throws ProcyonHttpException {
        CompletableFuture<Boolean> completableFuture = CompletableFuture.supplyAsync(() -> {
            String httpUrl = BASE_URL + url;

            HttpDelete httpDelete = new HttpDelete(httpUrl);
            httpDelete.setHeader("Accept", "application/json");

            try {
                CloseableHttpResponse response = httpClient.execute(httpDelete);

                if (response.getCode() != 200) throw new ProcyonHttpException(response.getReasonPhrase());

                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }, procyon.getExecutorService());

        return completableFuture.get();
    }
}
