package me.zloits.procyon.http;

import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;

public record ProcyonHttpResponse<T>(CloseableHttpResponse response, T obj) {
}
