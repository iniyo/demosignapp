package com.example.demosignapp.domain.common.exception.domain;


import com.example.demosignapp.infrastructure.common.constants.basecode.ErrorCode;

/**
 * 데이터가 존재하지 않을 때 발생하는 예외.
 * 예: 특정 ID로 조회했으나 데이터가 없는 경우.
 * `BusinessBaseException`을 상속받아 비즈니스 로직의 예외로 처리.
 */
public class NotFoundException extends BusinessBaseException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode.getDesc(), errorCode);
    }

    public NotFoundException(String message) {
        super(message, ErrorCode.NOT_FOUND);
    }

    public NotFoundException() {
        super(ErrorCode.NOT_FOUND);
    }
}