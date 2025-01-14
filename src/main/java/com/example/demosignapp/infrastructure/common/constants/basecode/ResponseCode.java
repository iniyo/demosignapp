package com.example.demosignapp.infrastructure.common.constants.basecode;

import org.springframework.http.HttpStatus;

public interface ResponseCode {

    String name();
    HttpStatus getHttpStatus();
    String getCode();
    String getDesc();
}
