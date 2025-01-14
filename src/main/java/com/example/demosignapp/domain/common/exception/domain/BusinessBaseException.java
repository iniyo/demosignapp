package com.example.demosignapp.domain.common.exception.domain;


import com.example.demosignapp.infrastructure.common.constants.basecode.ErrorCode;

/**
 * 애플리케이션에서 발생하는 비즈니스 로직 관련 예외의 기본 클래스.
 * 모든 비즈니스 로직 관련 예외는 이 클래스를 상속받아 처리.
 */

public class BusinessBaseException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessBaseException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessBaseException(ErrorCode errorCode) {
        super(errorCode.getDesc());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}