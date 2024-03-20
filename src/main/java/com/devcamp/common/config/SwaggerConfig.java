package com.devcamp.common.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@OpenAPIDefinition(
        info = @Info(
                title = "devcamp 인증&인가&결제 API",
                description = "devcamp 인증&인가&결제 서버 API 문서",
                version = "V1"
        )
)

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApi() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER).name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("Auth");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Auth", securityScheme))
                .security(List.of(securityRequirement));
    }
}
