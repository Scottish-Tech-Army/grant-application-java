package org.tfg.grant_java.service;


import org.tfg.grant_java.dto.AppUserDTO;
import org.tfg.grant_java.dto.CreateUserRequest;

import java.util.List;

/**
 * Service interface defining user management operations.
 *
 * <p>This service encapsulates business logic related to application users,
 * including user creation, retrieval, and charity-scoped user listing.</p>
 *
 * <p><b>Responsibilities:</b></p>
 * <ul>
 *   <li>Create new application users</li>
 *   <li>Retrieve user details by identifier</li>
 *   <li>List users associated with a specific charity</li>
 * </ul>
 *
 * <p>Implementations of this interface should enforce validation,
 * security constraints, and error handling.</p>
 */
public interface AppUserService {

    /**
     * Creates a new application user.
     *
     * <p>Implementations should handle validation, password encoding,
     * and default user initialization.</p>
     *
     * @param request request object containing user creation details
     * @return created user data transfer object
     */
    AppUserDTO createUser(CreateUserRequest request);

    /**
     * Retrieves a user by its unique identifier.
     *
     * @param id unique identifier of the user
     * @return user data transfer object
     */
    AppUserDTO getUserById(Long id);

    /**
     * Retrieves all users associated with the given charity.
     *
     * @param charityId identifier of the charity
     * @return list of users belonging to the specified charity
     */
    List<AppUserDTO> getUsersByCharity(Long charityId);
}
