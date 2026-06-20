package com.example.BankProject.Config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bankProjectOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Bank Management System API")
                .description(
                    "Complete REST API for the Bank Management System. " +
                    "Supports 4 roles: **SUPER_ADMIN**, **ADMIN**, **OFFICER**, **CUSTOMER**. " +
                    "All protected endpoints require a JWT Bearer token. " +
                    "Login via POST /auth/login to get your token."
                )
                .version("v1.0.0")
                .contact(new Contact()
                    .name("Bank Project Team")
                    .email("support@bankproject.com"))
                .license(new License()
                    .name("MIT License")))
            // ── JWT Bearer auth in Swagger UI ──────────────────────────────
            .addSecurityItem(new SecurityRequirement().addList("Bearer Auth"))
            .components(new Components()
                .addSecuritySchemes("Bearer Auth",
                    new SecurityScheme()
                        .name("Bearer Auth")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description(
                            "Paste your JWT access token here. " +
                            "Get it from POST /auth/login response."
                        )));
    }
}