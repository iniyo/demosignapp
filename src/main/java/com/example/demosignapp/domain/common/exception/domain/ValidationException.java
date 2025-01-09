package com.example.demosignapp.domain.common.exception.domain;


import com.example.demosignapp.infrastructure.common.constants.basecode.ErrorCode;

public class ValidationException extends BusinessBaseException {
    public ValidationException(String message) {
        super(message, ErrorCode.VALIDATION_FAILED);
    }
}