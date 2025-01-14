package com.example.demosignapp.domain.common.exception.domain;


import com.example.demosignapp.infrastructure.common.constants.basecode.ErrorCode;

public class NotAvailableException extends BusinessBaseException {
    public NotAvailableException(String message) {
        super(message, ErrorCode.NOT_AVAILABLE);
    }

    public NotAvailableException() {
        super(ErrorCode.NOT_AVAILABLE);
    }
}
