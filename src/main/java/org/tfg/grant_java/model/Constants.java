package org.tfg.grant_java.model;

/**
 * Centralized container for application-wide constant values.
 *
 * <p>This interface defines standard error codes used across the
 * grant application service to ensure consistency between
 * service-layer exceptions, configuration, and API error responses.</p>
 *
 * <p><b>Usage:</b></p>
 * <ul>
 *   <li>Referenced when throwing {@link org.tfg.grant_java.exception.ServiceException}</li>
 *   <li>Mapped to {@link org.tfg.grant_java.exception.ErrorDetails} via configuration</li>
 *   <li>Avoids hard-coded error strings scattered across the codebase</li>
 * </ul>
 *
 * <p><b>Note:</b> This interface is intended for constants only and
 * should not contain behavior.</p>
 */
public interface Constants {

    /**
     * Error code indicating that the requested application ID was not found.
     */
    String APPLICATION_ID_NOT_FOUND = "1001";

    /**
     * Error code indicating that the requested common data ID was not found.
     */
    String COMMON_DATA_ID_NOT_FOUND = "1002";

    /**
     * Error code indicating that no active charities are available.
     */
    String NO_ACTIVE_CHARITIES_AVAILABLE = "1003";

    /**
     * Error code indicating that the requested user does not exist.
     */
    String USER_NOT_PRESENT = "1004";
    String COMMON_DATA_JSON_INVALID = "1005";
    String INVALID_USERNAME_AND_PASSWORD = "1006";
}
