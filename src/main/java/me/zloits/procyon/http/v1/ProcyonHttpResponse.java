package me.zloits.procyon.http.v1;

import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;

public record ProcyonHttpResponse<T>(CloseableHttpResponse response, T result) {
}
