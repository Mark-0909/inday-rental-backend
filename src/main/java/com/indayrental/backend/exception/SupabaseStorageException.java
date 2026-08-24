package com.indayrental.backend.exception;

public class SupabaseStorageException extends RuntimeException {
    public SupabaseStorageException(String message) {
        super(message);
    }

    public SupabaseStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
