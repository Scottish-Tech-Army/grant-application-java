package org.tfg.grant_java.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.tfg.grant_java.exception.ErrorDetails;

import java.util.List;

/**
 * Configuration class that binds error definitions from application configuration.
 *
 * <p>
 * This class maps error metadata (error code, message, HTTP status, error type)
 * defined under the {@code error-config} prefix in {@code application.yml}
 * into a list of {@link ErrorDetails} objects.
 * </p>
 *
 * <p>
 * The loaded error definitions are used by the global exception handling
 * mechanism to translate business error codes into structured API error responses.
 * </p>
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "error-config")
public class ErrorConfig {

    /**
     * List of configured error definitions loaded from application properties.
     */
    private List<ErrorDetails> errorDetails;
}