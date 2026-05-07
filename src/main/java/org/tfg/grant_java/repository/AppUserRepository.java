package org.tfg.grant_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tfg.grant_java.entity.AppUser;

import java.util.Optional;

/**
 * Spring Data JPA repository for managing {@link AppUser} entities.
 *
 * <p>This repository provides CRUD operations for application users and
 * defines custom query methods commonly used during authentication and
 * user validation workflows.</p>
 *
 * <p><b>Key use cases:</b></p>
 * <ul>
 *   <li>Retrieve users by username</li>
 *   <li>Validate active users during login</li>
 *   <li>Support Spring Security authentication flows</li>
 * </ul>
 */
public interface AppUserRepository
        extends JpaRepository<AppUser, Long> {

    /**
     * Retrieves a user by username, regardless of active status.
     *
     * @param username the username to search for
     * @return an {@link Optional} containing the user if found
     */
    Optional<AppUser> findByUsername(String username);

    /**
     * Retrieves an active user by username.
     *
     * <p>Typically used during authentication to ensure the account
     * is enabled.</p>
     *
     * @param username the username to search for
     * @return an {@link Optional} containing the active user if found
     */
    Optional<AppUser> findByUsernameAndIsActiveTrue(String username);
}
