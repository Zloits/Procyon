package me.zloits.procyon.http.v1;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.zloits.procyon.Procyon;
import me.zloits.procyon.http.exception.ProcyonHttpException;
import me.zloits.procyon.util.GsonUtil;
import me.zloits.procyon.util.InstanceRegistry;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Getter
public class ProcyonHttpAPIV1 {

    private final Procyon procyon = InstanceRegistry.get(Procyon.class).orElseThrow();
    private final CloseableHttpClient httpClient;

    /**
     * Base URL for API requests.
     */
    @Setter
    private String BASE_URL = "https://api.example.com";

    /**
     * Constructor initializes the singleton instance and assigns the HTTP client.
     * @param httpClient The HTTP client used for requests.
     */
    public ProcyonHttpAPIV1(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Executes a GET request with an API key.
     * @param url Relative URL endpoint.
     * @param apiKey API key for authentication.
     * @param clazz Response object type.
     * @return Parsed response wrapped in ProcyonHttpResponse.
     */
    @SneakyThrows
    public <T> ProcyonHttpResponse<T> GET(String url, String apiKey, Class<T> clazz) {
        String httpUrl = BASE_URL + url;

        HttpGet httpGet = new HttpGet(httpUrl);
        httpGet.addHeader("X_API_KEY", apiKey);

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            String json = EntityUtils.toString(response.getEntity());
            return new ProcyonHttpResponse<>(response, GsonUtil.fromJson(json, clazz));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Executes a GET request without an API key.
     * @param url Relative URL endpoint.
     * @param clazz Response object type.
     * @return Parsed response wrapped in ProcyonHttpResponse.
     */
    @SneakyThrows
    public <T> ProcyonHttpResponse<T> GET(String url, Class<T> clazz) {
        String httpUrl = BASE_URL + url;

        try (CloseableHttpResponse response = httpClient.execute(new HttpGet(httpUrl))) {
            String json = EntityUtils.toString(response.getEntity());
            return new ProcyonHttpResponse<>(response, GsonUtil.fromJson(json, clazz));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Executes a POST request with an API key.
     * @param url Relative URL endpoint.
     * @param apiKey API key for authentication.
     * @param object Object to be sent as JSON.
     * @return Response wrapped in ProcyonHttpResponse.
     */
    @SneakyThrows
    public ProcyonHttpResponse<Object> POST(String url, String apiKey, Object object) {
        return executePostPutRequest(new HttpPost(BASE_URL + url), apiKey, object);
    }

    /**
     * Executes a POST request without an API key.
     * @param url Relative URL endpoint.
     * @param object Object to be sent as JSON.
     * @return Response wrapped in ProcyonHttpResponse.
     */
    @SneakyThrows
    public ProcyonHttpResponse<Object> POST(String url, Object object) {
        return executePostPutRequest(new HttpPost(BASE_URL + url), null, object);
    }

    /**
     * Executes a PUT request with an API key.
     * @param url Relative URL endpoint.
     * @param apiKey API key for authentication.
     * @param object Object to be sent as JSON.
     * @return Response wrapped in ProcyonHttpResponse.
     */
    @SneakyThrows
    public ProcyonHttpResponse<Object> PUT(String url, String apiKey, Object object) {
        return executePostPutRequest(new HttpPut(BASE_URL + url), apiKey, object);
    }

    /**
     * Executes a PUT request without an API key.
     * @param url Relative URL endpoint.
     * @param object Object to be sent as JSON.
     * @return Response wrapped in ProcyonHttpResponse.
     */
    @SneakyThrows
    public ProcyonHttpResponse<Object> PUT(String url, Object object) {
        return executePostPutRequest(new HttpPut(BASE_URL + url), null, object);
    }

    /**
     * Executes a DELETE request with an API key.
     * @param url Relative URL endpoint.
     * @param apiKey API key for authentication.
     * @return true if successful, otherwise false.
     */
    @SneakyThrows
    public boolean DELETE(String url, String apiKey) {
        return executeDeleteRequest(url, apiKey);
    }

    /**
     * Executes a DELETE request without an API key.
     * @param url Relative URL endpoint.
     * @return true if successful, otherwise false.
     */
    @SneakyThrows
    public boolean DELETE(String url) {
        return executeDeleteRequest(url, null);
    }

    /**
     * Helper method to execute POST and PUT requests.
     */
    private ProcyonHttpResponse<Object> executePostPutRequest(HttpUriRequestBase request, String apiKey, Object object) {
        request.addHeader("Content-Type", "application/json");
        if (apiKey != null) request.addHeader("X_API_KEY", apiKey);

        String json = GsonUtil.toJson(object);
        request.setEntity(new StringEntity(json, StandardCharsets.UTF_8));

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            ProcyonHttpResponse<Object> procyonHttpResponse = new ProcyonHttpResponse<>(response, object);
            if (response.getCode() != 200) throw new ProcyonHttpException(response.getReasonPhrase());
            return procyonHttpResponse;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Helper method to execute DELETE requests.
     */
    private boolean executeDeleteRequest(String url, String apiKey) {
        HttpDelete httpDelete = new HttpDelete(BASE_URL + url);
        httpDelete.setHeader("Accept", "application/json");
        if (apiKey != null) httpDelete.addHeader("X_API_KEY", apiKey);

        try (CloseableHttpResponse response = httpClient.execute(httpDelete)) {
            if (response.getCode() != 200) throw new ProcyonHttpException(response.getReasonPhrase());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}