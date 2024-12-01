package com.hrelix.app.utils;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@OpenAPIDefinition(
        info = @Info(
                title = "HRelix HR Management System",
                description = "API documentation for HRelix",
                version = "2.1.0"
        ),
        servers = {
                @Server(
                        description = "Production Environment",
                        url = "http://hrelix.blufin.co.in"
                ),
                @Server(
                        description = "Local Environment",
                        url = "http://localhost:8080"
                ),
        },
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT Auth",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .tags(Arrays.asList(
                        new Tag().name("Open Endpoints").description("Operations related to healthcheck and login"),
                        new Tag().name("Employee Endpoints").description("Operations related to Employee for HR and Employee"),
                        new Tag().name("Admin Endpoints").description("Operations related to Employee for Admin")
                ));
    }
}
