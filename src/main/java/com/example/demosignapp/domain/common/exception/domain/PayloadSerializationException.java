package com.example.demosignapp.domain.common.exception.domain;

public class PayloadSerializationException extends RuntimeException {
    public PayloadSerializationException(String message) {
        super(message);
    }

    public PayloadSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
