package org.tfg.grant_java.dto;

import lombok.Data;

/**
 * Request object for creating a new application user.
 *
 * Carries user registration details provided during user creation.
 */
@Data
public class CreateUserRequest {

    private String username;
    private String password;
    private String role;
    private Long charityId;
}
