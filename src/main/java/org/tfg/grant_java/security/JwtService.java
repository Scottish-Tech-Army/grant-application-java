package org.tfg.grant_java.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.tfg.grant_java.model.CustomUserDetails;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * Service for generating, parsing, and validating JSON Web Tokens (JWT).
 *
 * <p>This service issues JWTs for authenticated users and validates incoming tokens
 * for protected endpoints. Tokens are signed using HMAC-SHA256 (HS256) and include
 * application-specific claims such as roles and {@code charityId}.</p>
 *
 * <p><b>Token contents:</b></p>
 * <ul>
 *   <li><b>Subject</b>: username</li>
 *   <li><b>roles</b>: granted authorities for the user</li>
 *   <li><b>charityId</b>: charity scope associated with the user</li>
 *   <li><b>IssuedAt</b> and <b>Expiration</b>: token lifetime metadata</li>
 * </ul>
 *
 * <p><b>Security note:</b> The signing key is currently hard-coded as {@code SECRET_KEY}.
 * In production, this should be externalized (e.g., environment variable, vault/secret manager)
 * and rotated regularly.</p> 
 */
@Service
public class JwtService {

    /**
     * Secret key used to sign JWT tokens (HS256).
     *
     * <p><b>Important:</b> Do not hard-code secrets in source code for production deployments.</p> 
     */
    private static final String SECRET_KEY = "w87h1LkG1+mYFvF8x8BtGmGd6G2XyHkWy9vWJ7QrPzc=";

    /**
     * Generates a signed JWT token for the provided authenticated user.
     *
     * <p>The token includes username as subject and embeds {@code roles} and {@code charityId}
     * claims. The expiration is set to 1 hour from issuance time.</p>
     *
     * @param userDetails authenticated user details
     * @return signed JWT token string
     */
    public String generateToken(CustomUserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities())
                .claim("charityId", userDetails.getCharityId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hr
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the username (JWT subject) from the token.
     *
     * @param token JWT token string
     * @return username stored as subject 
     */
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Extracts the charity identifier claim from the token.
     *
     * @param token JWT token string
     * @return charityId claim value 
     */
    public Long extractCharityId(String token) {
        return getClaims(token).get("charityId", Long.class);
    }

    /**
     * Validates a token against the expected user details.
     *
     * <p>A token is considered valid if:</p>
     * <ul>
     *   <li>the username matches the token subject</li>
     *   <li>the charityId matches the token claim</li>
     *   <li>the token is not expired</li>
     * </ul>
     *
     * @param token JWT token string
     * @param userDetails expected authenticated user details
     * @return {@code true} if token is valid; {@code false} otherwise 
     */
    public boolean isTokenValid(String token, CustomUserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername())
                && extractCharityId(token).equals(userDetails.getCharityId())
                && !isExpired(token);
    }

    /**
     * Checks whether the token is expired based on its {@code exp} claim.
     *
     * @param token JWT token string
     * @return {@code true} if expired; {@code false} otherwise 
     */
    private boolean isExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    /**
     * Parses the JWT and returns its claims payload.
     *
     * @param token JWT token string
     * @return parsed {@link Claims} 
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Builds the signing key used for HS256 token signing/verification.
     *
     * @return HMAC signing key 
     */
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }
}
