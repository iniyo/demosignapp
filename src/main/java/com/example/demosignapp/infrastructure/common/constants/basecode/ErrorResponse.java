package com.example.demosignapp.infrastructure.common.constants.basecode;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private String code;
    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL) // null인 경우 직렬화하지 않음
    private Object fieldErrors;

    private ErrorResponse(final ErrorCode code) {
        this.description = code.getDesc();
        this.code = code.getCode();
    }

    private ErrorResponse(final ErrorCode code, final String description) {
        this.description = description;
        this.code = code.getCode();
    }

    private ErrorResponse(final ErrorCode errorCode, Object fieldErrors) {
        this(errorCode);
        this.fieldErrors = fieldErrors;
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode);
    }

    public static ErrorResponse of(ErrorCode errorCode, Object fieldErrors) {
        return new ErrorResponse(errorCode, fieldErrors);
    }

    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return new ErrorResponse(errorCode, message);
    }

    public static ErrorResponse failure(BindingResult bindingResult, ErrorCode errorCode) {
        return new ErrorResponse(errorCode, bindingResult.getFieldErrors());
    }
}
