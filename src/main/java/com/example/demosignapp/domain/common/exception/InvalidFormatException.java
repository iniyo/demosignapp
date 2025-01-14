package com.example.demosignapp.domain.common.exception;


import com.example.demosignapp.infrastructure.common.constants.basecode.ErrorCode;

public class InvalidFormatException extends InputValidationException {
    public InvalidFormatException(String fieldName) {
        super(fieldName + " has an invalid format.", ErrorCode.VALIDATION_FAILED);
    }
}