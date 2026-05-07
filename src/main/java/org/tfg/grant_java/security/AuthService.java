package org.tfg.grant_java.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.tfg.grant_java.exception.ServiceException;
import org.tfg.grant_java.model.Constants;
import org.tfg.grant_java.model.CustomUserDetails;

/**
 * Service responsible for authenticating users and issuing JWT tokens.
 *
 * <p>This service delegates credential verification to Spring Security's
 * {@link AuthenticationManager}. On successful authentication, it applies
 * an additional business rule to ensure the user belongs to the requested
 * charity context, then generates a JWT using {@link JwtService}.</p>
 *
 * <p><b>Security considerations:</b></p>
 * <ul>
 *   <li>Passwords and JWT tokens are intentionally not logged.</li>
 *   <li>Authentication failures and business rule rejections are logged at WARN level.</li>
 * </ul>
 */
@Slf4j
@AllArgsConstructor
@Service
public class AuthService {

    /**
     * Spring Security authentication manager used to validate username/password.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Service used to generate JWT tokens for authenticated users.
     */
    private final JwtService jwtService;

    /**
     * Authenticates a user and returns a JWT token if the login is valid.
     *
     * <p>Flow:</p>
     * <ol>
     *   <li>Authenticate username/password via {@link AuthenticationManager}</li>
     *   <li>Verify the authenticated user's charity matches the requested {@code charityId}</li>
     *   <li>Generate and return a JWT token</li>
     * </ol>
     *
     * @param username  username provided by the user
     * @param password  password provided by the user (not logged)
     * @param charityId charity context the user is attempting to log in under
     * @return generated JWT token for the authenticated user
     * @throws RuntimeException if authentication fails (invalid credentials)
     * @throws ServiceException if the user is authenticated but the charity context mismatches
     */
    public String login(String username, String password, Long charityId) {

        // password is intentionally not logged
        log.info("Authentication attempt | username={} | charityId={}", username, charityId);

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        if (!auth.isAuthenticated()) {
            // expected auth failure
            log.warn("Authentication failed | username={} | charityId={}", username, charityId);
            throw new RuntimeException("Invalid login");
        }

        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

        if (!user.getCharityId().equals(charityId)) {
            // expected business rule failure (user exists, but charity mismatch)
            log.warn(
                    "Authentication rejected due to charity mismatch | username={} | requestedCharityId={} | userCharityId={}",
                    username, charityId, user.getCharityId()
            );
            throw new ServiceException(Constants.USER_NOT_PRESENT);
        }

        String token = jwtService.generateToken(user);

        // token is intentionally not logged
        log.info("Authentication successful | username={} | charityId={}", username, charityId);

        return token;
    }
}