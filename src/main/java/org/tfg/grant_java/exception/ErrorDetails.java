package org.tfg.grant_java.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing standardized error details returned by the application.
 *
 * <p>This class is typically used in exception handling and API error
 * responses to provide consistent, client-friendly error information.</p>
 *
 * <p><b>Design notes:</b></p>
 * <ul>
 *   <li>Designed as a lightweight, immutable-style data carrier using Lombok</li>
 *   <li>Separates transport-level HTTP status from API error payload</li>
 *   <li>Safe to expose to API consumers</li>
 * </ul>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {

    /**
     * Application-specific error code.
     *
     * <p>Used for programmatic error handling and troubleshooting.</p>
     */
    private String errorCode;

    /**
     * Human-readable error message describing the failure.
     */
    private String message;

    /**
     * High-level classification of the error (e.g., VALIDATION, BUSINESS, SYSTEM).
     */
    private String errorType;

    /**
     * HTTP status associated with the error.
     *
     * <p>Ignored during JSON serialization and used internally by
     * exception handlers.</p>
     */
    @JsonIgnore
    private int httpStatus;

}
