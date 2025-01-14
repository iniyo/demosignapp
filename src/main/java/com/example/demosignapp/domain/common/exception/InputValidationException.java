package com.example.demosignapp.domain.common.exception;


import com.example.demosignapp.infrastructure.common.constants.basecode.ErrorCode;

public class InputValidationException extends RuntimeException {
    private final ErrorCode errorCode;

    public InputValidationException(String message) {
        super(message);
        this.errorCode = ErrorCode.BAD_REQUEST;
    }

    protected InputValidationException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
