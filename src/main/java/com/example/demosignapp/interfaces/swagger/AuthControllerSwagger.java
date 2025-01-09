package com.example.demosignapp.interfaces.swagger;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.example.demosignapp.interfaces.swagger.SwaggerUtils.addErrorResponses;
import static com.example.demosignapp.interfaces.swagger.SwaggerUtils.apiResultSchema;

/**
 * AuthController의 각 메서드에 대한 Swagger 문서화
 */
@Component
class AuthControllerSwagger implements OperationCustomizer {

    // 생성자에서 DTO 스키마 등록
    public AuthControllerSwagger(SwaggerUtils swaggerUtils) {
        // 예: DTO 스키마 등록
        swaggerUtils.addDtoSchema("TokenReissueRequest", tokenReissueRequestSchema());
        swaggerUtils.addDtoSchema("TokenReissueResponse", tokenReissueResponseSchema());
        swaggerUtils.addDtoSchema("LogoutRequest", logoutRequestSchema());
        swaggerUtils.addDtoSchema("LogoutResponse", logoutResponseSchema());

        // 예: 공통 래퍼 (ApiResult 등)가 필요하다면
        swaggerUtils.addDtoSchema("ApiResultTokenReissueResponse",
                apiResultSchema("TokenReissueResponse"));
    }

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        String methodName = handlerMethod.getMethod().getName();

        switch (methodName) {
            case "reissue":
                operation.setSummary("만료된 AccessToken 재발급");
                operation.setDescription("RefreshToken으로 새로운 AccessToken을 발급받습니다.");
                // 예: 에러 응답 추가
                addErrorResponses(operation, "400", "RefreshToken 만료 또는 없음");
                break;

            case "logout":
                operation.setSummary("전체 로그아웃");
                operation.setDescription("해당 사용자의 모든 토큰을 무효화합니다.");
                break;

            case "logoutSingle":
                operation.setSummary("특정 토큰만 로그아웃");
                operation.setDescription("주어진 AccessToken만 무효화합니다.");
                break;

            case "authSuccess":
                operation.setSummary("소셜 로그인 성공 후 처리");
                break;
        }

        // 공통 에러 추가 가능
        addErrorResponses(operation, "401", "인증 실패");
        addErrorResponses(operation, "403", "권한 부족");
        return operation;
    }

    /**
     * DTO 스키마들 정의
     */
    private Schema<?> tokenReissueRequestSchema() {
        Map<String, Schema<?>> props = new LinkedHashMap<>();
        props.put("oldAccessToken", new Schema<String>()
                .type("string")
                .description("기존 만료된 AccessToken")
                .example("Bearer ..."));
        Schema<Object> schema = new Schema<>();
        schema.setType("object");
        schema.setDescription("만료된 토큰 재발급 요청 DTO");
        schema.setProperties(Collections.unmodifiableMap(props));
        return schema;
    }

    private Schema<?> tokenReissueResponseSchema() {
        Map<String, Schema<?>> props = new LinkedHashMap<>();
        props.put("newAccessToken", new Schema<String>()
                .type("string")
                .description("새로 발급된 AccessToken")
                .example("Bearer ..."));
        props.put("message", new Schema<String>()
                .type("string")
                .description("결과 메시지")
                .example("재발급 성공"));
        Schema<Object> schema = new Schema<>();
        schema.setType("object");
        schema.setDescription("재발급 응답 DTO");
        schema.setProperties(Collections.unmodifiableMap(props));
        return schema;
    }

    private Schema<?> logoutRequestSchema() {
        Map<String, Schema<?>> props = new LinkedHashMap<>();
        props.put("memberKey", new Schema<String>()
                .type("string")
                .description("로그아웃 대상 사용자")
                .example("member-xxxxx"));
        props.put("accessToken", new Schema<String>()
                .type("string")
                .description("단일 토큰만 무효화시 필요")
                .example("Bearer ..."));

        Schema<Object> schema = new Schema<>();
        schema.setType("object");
        schema.setDescription("로그아웃 요청 DTO");
        schema.setProperties(Collections.unmodifiableMap(props));
        return schema;
    }

    private Schema<?> logoutResponseSchema() {
        Map<String, Schema<?>> props = new LinkedHashMap<>();
        props.put("message", new Schema<String>()
                .type("string")
                .description("로그아웃 결과 메시지")
                .example("로그아웃 완료(모든 토큰 무효화)"));
        props.put("invalidatedToken", new Schema<String>()
                .type("string")
                .description("무효화된 토큰 값")
                .example("Bearer ...")
                .nullable(true));

        Schema<Object> schema = new Schema<>();
        schema.setType("object");
        schema.setDescription("로그아웃 응답 DTO");
        schema.setProperties(Collections.unmodifiableMap(props));
        return schema;
    }
}
