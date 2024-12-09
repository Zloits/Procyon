package me.zloits.procyon.http.exception;

import java.io.IOException;

public class ProcyonHttpException extends IOException {

    public ProcyonHttpException(String message) {
        super(message);
    }
}
