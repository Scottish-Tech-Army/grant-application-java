package org.tfg.grant_java.exception;

import lombok.Data;

import java.io.Serial;

/**
 * Custom runtime exception representing a business or service-level error.
 *
 * <p>This exception is used to propagate controlled, application-specific
 * error conditions identified by an {@code errorCode}. The error code is later
 * resolved to a user-facing {@link ErrorDetails} instance by the
 * {@link GlobalExceptionHandler}.</p>
 *
 * <p><b>Design intent:</b></p>
 * <ul>
 *   <li>Encapsulates only an error code, keeping exceptions lightweight</li>
 *   <li>Avoids exposing internal stack traces for business failures</li>
 *   <li>Enables centralized error mapping via configuration</li>
 * </ul>
 */
@Data
public class ServiceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Application-specific error code used to resolve error details.
     */
    private final String errorCode;

    /**
     * Creates a new {@code ServiceException} with the given error code.
     *
     * @param errorCode unique identifier representing the error condition
     */
    public ServiceException(String errorCode) {
        this.errorCode = errorCode;
    }
}
