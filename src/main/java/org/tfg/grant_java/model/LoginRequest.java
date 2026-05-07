package org.tfg.grant_java.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Request model representing login credentials submitted by a user.
 *
 * <p>This DTO is used during authentication to capture the information
 * required to validate a user and establish their security context.</p>
 *
 * <p><b>Validation rules:</b></p>
 * <ul>
 *   <li>{@code username} must not be blank</li>
 *   <li>{@code password} must not be blank</li>
 *   <li>{@code charityId} must be provided to scope the login</li>
 * </ul>
 */
@Data
public class LoginRequest {

    /**
     * Username used for authentication.
     */
    @NotBlank(message = "Username is required")
    private String username;

    /**
     * Password used for authentication.
     */
    @NotBlank(message = "Password is required")
    private String password;

    /**
     * Identifier of the charity the user is attempting to log in under.
     *
     * <p>This ensures users are authenticated within the correct
     * charity context.</p>
     */
    @NotNull(message = "Charity ID is required")
    private Long charityId;

}
