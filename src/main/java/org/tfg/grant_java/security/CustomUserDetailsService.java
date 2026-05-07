package org.tfg.grant_java.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.tfg.grant_java.entity.AppUser;
import org.tfg.grant_java.model.Constants;
import org.tfg.grant_java.model.CustomUserDetails;
import org.tfg.grant_java.repository.AppUserRepository;

import java.util.List;

/**
 * Custom {@link UserDetailsService} implementation used by Spring Security to load user details.
 *
 * <p>This service fetches an active {@link AppUser} by username from {@link AppUserRepository}
 * and converts it into a {@link CustomUserDetails} instance required by Spring Security for
 * authentication and authorization.</p>
 *
 * <p><b>Key responsibilities:</b></p>
 * <ul>
 *   <li>Loads only active users via {@code findByUsernameAndIsActiveTrue}</li>
 *   <li>Throws {@link UsernameNotFoundException} when user is missing or inactive</li>
 *   <li>Maps the user's role to a Spring Security authority prefixed with {@code ROLE_}</li>
 *   <li>Adds {@code charityId} into {@link CustomUserDetails} for charity-scoped access</li>
 * </ul>
 *
 * <p><b>Logging notes:</b></p>
 * <ul>
 *   <li>Passwords are intentionally not logged.</li>
 *   <li>Missing/inactive users are logged at WARN level.</li>
 * </ul>
 */
@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Repository used to retrieve {@link AppUser} records.
     */
    private final AppUserRepository repo;

    /**
     * Cached charity id associated with the last loaded user.
     *
     * <p><b>Note:</b> This value is mutable and shared across requests because the service is
     * typically a singleton bean. Prefer reading charityId from {@link CustomUserDetails}
     * instead of relying on this field.</p>
     */
    private Long charityId;

    /**
     * Creates a new {@code CustomUserDetailsService}.
     *
     * @param repo repository for user lookup
     */
    public CustomUserDetailsService(AppUserRepository repo) {
        this.repo = repo;
    }

    /**
     * Loads an active user by username and adapts it to {@link CustomUserDetails}.
     *
     * @param username the username identifying the user whose data is required
     * @return fully populated {@link CustomUserDetails} instance
     * @throws UsernameNotFoundException if the user does not exist or is inactive
     */
    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("Loading user details | username={}", username);

        AppUser user = repo.findByUsernameAndIsActiveTrue(username)
                .orElseThrow(() -> {
                    log.warn("User not found or inactive | username={}", username);
                    return new UsernameNotFoundException(Constants.INVALID_USERNAME_AND_PASSWORD);
                });

        this.charityId = user.getCharityId();

        // password intentionally not logged
        log.info(
                "User details loaded | username={} | charityId={} | role={}",
                user.getUsername(),
                user.getCharityId(),
                user.getRole()
        );

        return new CustomUserDetails(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole())),
                user.getCharityId()
        );
    }
}