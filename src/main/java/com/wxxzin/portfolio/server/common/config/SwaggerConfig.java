package com.wxxzin.portfolio.server.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("BearerToken", createBearerTokenScheme())
                        .addSecuritySchemes("CookieAuth", createCookieAuthScheme())
                )
                .info(apiInfo())
                .addSecurityItem(new SecurityRequirement().addList("BearerToken").addList("CookieAuth"));
    }

    private Info apiInfo() {
        return new Info()
                .title("Portfolio API")
                .description("API의 Docs와 Test를 위한 페이지입니다.")
                .version("1.0.0");
    }

    private SecurityScheme createBearerTokenScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("Bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");
    }

    private SecurityScheme createCookieAuthScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.COOKIE)
                .name("refreshToken");
    }
}
