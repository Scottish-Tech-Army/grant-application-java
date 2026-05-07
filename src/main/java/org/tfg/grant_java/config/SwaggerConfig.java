package org.tfg.grant_java.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Grant Application")
                        .version("1.0")
                        .description("API documentation for Grant Application"))

                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", bearerAuth)
                        .addSchemas("LoginRequest", loginSchema)
                        .addSchemas("AppUser", appUser)
                        .addSchemas("CommonDataDTO", commonDataDTO)
                        .addSchemas("ApplicationDTO", applicationDTO)
                        .addSchemas("ApplicationResponse", applicationResponse)
                        .addSchemas("ApplicationJsonDataResponse", applicationJsonDataResponse)
                        .addSchemas("CharityDTO", charityDTO)
                );
    }

    SecurityScheme bearerAuth = new SecurityScheme()
            .name("Authorization")
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER);

    Schema<?> loginSchema = new Schema<>()
            .example(Map.of(
                    "username", "user1",
                    "password", "pass123",
                    "charityId", 1
            ));
    Schema<?> appUser = new Schema<>()
            .example(Map.of(
                    "username", "user1",
                    "password", "pass123",
                    "charityId", 1,
                    "role", "USER"
            ));
    Schema<?> commonDataDTO = new Schema<>()
            .example(Map.of(
                    "id", 1,
                    "charityId", 1,
                    "version", "1.0",
                    "dataJson", "{\"key\":\"value\"}",
                    "templateTitle", "Grant Application Template",
                    "description", "This is a description of the common data."
            ));
    Schema<?> applicationDTO = new Schema<>()
            .example(Map.of(
                    "applicationNumber", "APP-12345",
                    "projectName", "Community Development Project",
                    "funderName", "Global Fund",
                    "charityId", 1,
                    "commonDataId", 1,
                    "status", "Pending",
                    "selectedCommonKeys", "[1,2]",
                    "commonDataJson", "{\"key\":\"value\"}",
                    "applicationDataJson", "{\"field\":\"data\"}",
                    "comments", "This is a sample application."
            ));
    Schema<?> applicationResponse = new Schema<>()
            .example(Map.of(
                    "id", 1,
                    "applicationNumber", "APP-12345",
                    "projectName", "Community Development Project",
                    "funderName", "Global Fund",
                    "charityId", 1,
                    "commonDataId", 2,
                    "status", "Approved",
                    "modifiedAt", "2023-10-25T12:34:56"
            ));
    Schema<?> applicationJsonDataResponse = new Schema<>()
            .example(Map.ofEntries(
                    Map.entry("id", 1),
                    Map.entry("applicationNumber", "APP-12345"),
                    Map.entry("projectName", "Community Development Project"),
                    Map.entry("funderName", "Global Fund"),
                    Map.entry("charityId", 1),
                    Map.entry("commonDataId", 2),
                    Map.entry("status", "Approved"),
                    Map.entry("selectedCommonKeys", "[1,2]"),
                    Map.entry("applicationDataJson", "{\"field\":\"data\"}"),
                    Map.entry("version", "1.0"),
                    Map.entry("templateTitle", "Grant Application Template"),
                    Map.entry("dataJson", "{\"key\":\"value\"}"),
                    Map.entry("comments", "This is a sample application."),
                    Map.entry("createdBy", "admin"),
                    Map.entry("modifiedBy", "editor"),
                    Map.entry("createdAt", "2023-10-25T12:34:56"),
                    Map.entry("modifiedAt", "2023-10-26T14:20:30")
            ));
    Schema<?> charityDTO = new Schema<>()
            .example(Map.of(
                    "id", 1L,
                    "name", "Charity Organization",
                    "isActive", true,
                    "createdBy", "admin",
                    "createdAt", "2023-10-25T12:34:56"
            ));
}
