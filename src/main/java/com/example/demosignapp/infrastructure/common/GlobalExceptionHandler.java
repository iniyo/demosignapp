package com.example.demosignapp.infrastructure.common;


import com.example.demosignapp.domain.common.exception.domain.BusinessBaseException;
import com.example.demosignapp.infrastructure.common.constants.basecode.ErrorCode;
import com.example.demosignapp.infrastructure.common.constants.basecode.ErrorResponse;
import com.example.demosignapp.infrastructure.jwt.TokenException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Global Exception Handler
 * 모든 컨트롤러에서 발생하는 예외를 전역적으로 처리하여 일관된 에러 응답을 제공합니다.
 */
@Slf4j
@ControllerAdvice
class GlobalExceptionHandler {

    /**
     * 유효성 검사 실패 예외 처리
     *
     * @param e MethodArgumentNotValidException
     * @return 에러 응답 및 HTTP 상태 코드
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        log.warn("Validation 실패: {}", e.getMessage());

        // 필드 에러를 Map 형태로 변환
        Map<String, String> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fieldError -> Optional.of(fieldError.getField()).orElse("unknownField"),
                        fieldError -> Optional.ofNullable(fieldError.getDefaultMessage()).orElse("Unknown error")
                ));

        return createErrorResponseEntity(ErrorCode.BAD_REQUEST, fieldErrors);
    }

    /**
     *
     * @param e RuntimeError
     * @return 에러 응답 및 HTTP 상태 코드
     */
    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorResponse> handleRuntimeErrorException(RuntimeException e) {
        log.warn("RuntimeError 발생: {}", e.getMessage());
        return createErrorResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ErrorResponse> handleTokenException(TokenException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of(ErrorCode.UNAUTHORIZED, e.getMessage()));
    }

    /**
     * 보안 관련 예외 처리
     *
     * @param e SecurityException
     * @return 에러 응답 및 HTTP 상태 코드
     */
    @ExceptionHandler(SecurityException.class)
    protected ResponseEntity<ErrorResponse> handleSecurityException(SecurityException e) {
        log.error("SecurityException 발생", e);
        return createErrorResponseEntity(ErrorCode.FORBIDDEN, e.getMessage());
    }

    /**
     * 리소스 찾을 수 없음 예외 처리
     *
     * @param e       NoResourceFoundException
     * @param request HttpServletRequest
     * @return 에러 응답 및 HTTP 상태 코드
     */
    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<ErrorResponse> handleResourceNotFoundException(NoResourceFoundException e, HttpServletRequest request) {
        String requestURL = request.getRequestURL().toString();
        if (requestURL.endsWith("favicon.ico")) {
            log.info("Favicon 요청 무시: {}", requestURL);
            return ResponseEntity.notFound().build();
        }
        log.error("NoResourceFoundException 발생: {}", requestURL);
        return createErrorResponseEntity(ErrorCode.NOT_FOUND, e.getMessage());
    }

    /**
     * 지원되지 않는 HTTP 메서드 예외 처리
     *
     * @param e HttpRequestMethodNotSupportedException
     * @return 에러 응답 및 HTTP 상태 코드
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleMethodNotAllowedException(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException 발생", e);
        return createErrorResponseEntity(ErrorCode.METHOD_NOT_ALLOWED, e.getMessage());
    }

    /**
     * 잘못된 JSON 형식 예외 처리
     *
     * @param e HttpMessageNotReadableException
     * @return 에러 응답 및 HTTP 상태 코드
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException 발생: {}", e.getMessage());
        String errorMessage = "JSON 요청 형식이 올바르지 않습니다. 요청 본문을 확인하세요.";
        return createErrorResponseEntity(ErrorCode.BAD_REQUEST, errorMessage);
    }

    /**
     * 비즈니스 로직 예외 처리
     *
     * @param e BusinessBaseException
     * @return 에러 응답 및 HTTP 상태 코드
     */
    @ExceptionHandler(BusinessBaseException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessBaseException e) {
        log.error("BusinessException 발생", e);
        return createErrorResponseEntity(e.getErrorCode(), e.getMessage());
    }


//    /**
//     * Firebase messaging 예외 처리
//     * @param e FirebaseMessagingException
//     * @return 에러 응답 및 HTTP 상태 코드
//     */
//    @ExceptionHandler(FirebaseMessagingException.class)
//    public ResponseEntity<ErrorResponse> handleFirebaseMessagingException(FirebaseMessagingException e) {
//        log.error("Firebase Messaging Exception 발생: {}, ErrorCode: {}", e.getMessage(), e.getMessagingErrorCode());
//
//        if ("UNREGISTERED".equals(e.getMessagingErrorCode().name())) {
//            log.warn("Invalid FCM Token detected: {}", e.getMessage());
//            return createErrorResponseEntity(ErrorCode.UNAUTHORIZED_TOKEN_ERROR, "The token is unregistered or invalid.");
//        }
//
//        return createErrorResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to send notification: " + e.getMessagingErrorCode());
//    }

    /**
     * 알 수 없는 예외 처리
     *
     * @param e Exception
     * @return 에러 응답 및 HTTP 상태 코드
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        log.error("Unhandled Exception 발생", e);
        return createErrorResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    /**
     * 공통 에러 응답 생성 메서드
     *
     * @param errorCode 에러 코드
     * @param details   추가 정보 (null 허용)
     * @return 에러 응답 및 HTTP 상태 코드
     */
    private ResponseEntity<ErrorResponse> createErrorResponseEntity(ErrorCode errorCode, Object details) {
        return new ResponseEntity<>(
                ErrorResponse.of(errorCode, details),
                errorCode.getHttpStatus()
        );
    }

    /**
     * 공통 에러 응답 생성 메서드 (기본 메시지 사용)
     *
     * @param errorCode 에러 코드
     * @return 에러 응답 및 HTTP 상태 코드
     */
    private ResponseEntity<ErrorResponse> createErrorResponseEntity(ErrorCode errorCode) {
        return createErrorResponseEntity(errorCode, null);
    }
}
