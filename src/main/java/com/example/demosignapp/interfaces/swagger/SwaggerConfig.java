package com.example.demosignapp.interfaces.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 전역 Swagger(OpenAPI) 설정
 */
@Configuration
class SwaggerConfig {

    // 1) 전역 Components 빈을 만들어서, 나중에 Customizer들이 주입받을 수 있도록
    @Bean
    public Components swaggerComponents() {
        return new Components();
    }

    // 2) OpenAPI Bean
    @Bean
    public OpenAPI customOpenAPI(Components swaggerComponents) {
        return new OpenAPI()
                // API 정보
                .info(new Info()
                        .title("Demo Sign App API")
                        .version("v1.0")
                        .description("JWT + OAuth2 + Redis DDD 예시 프로젝트"))
                // JWT Bearer 인증을 위한 설정
                .components(
                        swaggerComponents.addSecuritySchemes("BearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"));
    }

}
