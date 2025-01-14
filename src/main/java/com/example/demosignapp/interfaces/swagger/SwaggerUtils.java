package com.example.demosignapp.interfaces.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 공통 Swagger 문서화 유틸
 */
@Component
@RequiredArgsConstructor
public class SwaggerUtils {

    private final Components components;

    /**
     * 전역 스키마(예: DTO) 등록
     */
    public void addDtoSchema(String name, Schema<?> schema) {
        components.addSchemas(name, schema);
    }

    /**
     * 특정 Endpoint에 커스텀 에러 응답 추가
     */
    public static void addErrorResponses(Operation operation, String httpStatusCode, String description) {
        operation.getResponses().addApiResponse(
                httpStatusCode,
                createApiResponse(description)
        );
    }

    /**
     * ApiResponse 생성
     */
    public static ApiResponse createApiResponse(String desc) {
        // 단순 예: JSON 예시를 넣어줄 수도 있음
        return new ApiResponse()
                .description(desc)
                .content(new Content().addMediaType("application/json",
                        new MediaType().schema(new Schema<>().example(Map.of("error", desc)))));
    }

    /**
     * 예: "ApiResult" 형태의 응답 스키마(공통) 생성
     */
    public static Schema<Object> apiResultSchema(String dataSchemaRef) {
        Map<String, Schema<?>> properties = new LinkedHashMap<>();
        properties.put("code", new Schema<>().type("string").example("2000000"));
        properties.put("message", new Schema<>().type("string").example("Success"));
        properties.put("data", new Schema<>().$ref("#/components/schemas/" + dataSchemaRef));

        Schema<Object> schema = new Schema<>();
        schema.setType("object");
        schema.setDescription("공통 API 응답 래퍼");
        schema.setProperties(Collections.unmodifiableMap(properties));
        return schema;
    }
}
