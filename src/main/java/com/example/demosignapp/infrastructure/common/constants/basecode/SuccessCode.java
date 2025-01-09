package com.example.demosignapp.infrastructure.common.constants.basecode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
public enum SuccessCode implements ResponseCode {
    OK(HttpStatus.OK,"20000000", "요청 성공"),
    CREATED(HttpStatus.CREATED, "20100000", "생성 성공"),
    ACCEPTED(HttpStatus.ACCEPTED, "20200000", "처리가 완료되지 않음"),
    NO_CONTENT(HttpStatus.NO_CONTENT, "20400000", "요청 성공(컨텐츠 없음)"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String desc;

    SuccessCode(HttpStatus httpStatus, String code, String desc) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.desc = desc;
    }

    public static SuccessCode valueOf(int httpStatus) {
        return Arrays.stream(values())
            .filter(i -> i.httpStatus.value() == httpStatus)
            .findFirst()
            .orElse(null);
    }

}
