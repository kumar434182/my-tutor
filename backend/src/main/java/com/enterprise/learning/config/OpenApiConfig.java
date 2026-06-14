package com.enterprise.learning.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Enterprise Learning Platform API",
        version = "1.0.0",
        description = "REST API for Enterprise Training & AI Learning Platform",
        contact = @Contact(
            name = "Enterprise Learning Team",
            email = "support@learningplatform.com"
        ),
        license = @License(
            name = "Enterprise License",
            url = "https://www.learningplatform.com/license"
        )
    )
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "JWT authentication token"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .servers(List.of(
                new Server()
                    .url("http://localhost:8080")
                    .description("Local Development"),
                new Server()
                    .url("https://api.learningplatform.com")
                    .description("Production")
            ));
    }
}
