package org.tfg.grant_java.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Grant Application",
                version = "v1",
                description = "APIs to fetch user predefined fields and questions"
        )
)
public class OpenApiConfig {

}
