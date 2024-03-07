package com.librarymanagementsystem.openapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Library Management System API",
                description = "Library Management System API",
                version = "1.0"
        ))
public class OpenAPIConfig {
    @Bean
    public org.springdoc.core.models.GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-api")
                .pathsToMatch("api/v1/library/management/system/**")
                .build();
    }

    @Bean
    public GroupedOpenApi privateApi() {
        return GroupedOpenApi.builder()
                .group("private-api")
                .packagesToScan("com.librarymanagementsystem.web.controller")
                .build();
    }
}
