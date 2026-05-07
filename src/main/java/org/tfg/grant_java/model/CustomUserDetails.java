package org.tfg.grant_java.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Custom implementation of Spring Security {@link User} that
 * enriches the authenticated user context with application-specific data.
 *
 * <p>This class is used by Spring Security during authentication and
 * authorization to represent the currently logged-in user.</p>
 *
 * <p><b>Enhancement over default User:</b></p>
 * <ul>
 *   <li>Adds {@code charityId} to associate the authenticated user with a charity</li>
 *   <li>Allows downstream services and controllers to perform
 *       charity-scoped authorization and data access</li>
 * </ul>
 *
 * <p>This object is typically stored in the Spring Security
 * {@code SecurityContext} after successful authentication.</p>
 */
@Getter
@Setter
public class CustomUserDetails extends User {

    /**
     * Identifier of the charity associated with the authenticated user.
     */
    private Long charityId;

    /**
     * Creates a new {@code CustomUserDetails} instance.
     *
     * @param username     the username used for authentication
     * @param password     the encrypted user password
     * @param authorities  granted authorities (roles/permissions)
     * @param charityId    identifier of the associated charity
     */
    public CustomUserDetails(
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            Long charityId) {

        super(username, password, authorities);
        this.charityId = charityId;
    }
}
