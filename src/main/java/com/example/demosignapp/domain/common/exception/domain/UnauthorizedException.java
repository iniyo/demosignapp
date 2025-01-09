package com.example.demosignapp.domain.common.exception.domain;


import com.example.demosignapp.infrastructure.common.constants.basecode.ErrorCode;

public class UnauthorizedException extends BusinessBaseException {
    public UnauthorizedException(String message) {
        super(message, ErrorCode.UNAUTHORIZED);
    }
}
