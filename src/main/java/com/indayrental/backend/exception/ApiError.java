package com.indayrental.backend.exception;

import java.time.Instant;

public class ApiError {
    private final String message;
    private final String code;
    private final Instant timestamp;

    public ApiError(String message, String code, Instant timestamp) {
        this.message = message;
        this.code = code;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
