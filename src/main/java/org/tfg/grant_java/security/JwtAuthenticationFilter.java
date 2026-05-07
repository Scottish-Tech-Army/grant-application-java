package org.tfg.grant_java.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.tfg.grant_java.model.CustomUserDetails;

import java.io.IOException;

/**
 * Spring Security filter that authenticates incoming HTTP requests using a JWT bearer token.
 *
 * <p>This filter runs once per request (via {@link OncePerRequestFilter}) and performs the following:</p>
 * <ol>
 *   <li>Reads the {@code Authorization} header and checks for a {@code Bearer } token</li>
 *   <li>Extracts the username from the JWT using {@link JwtService}</li>
 *   <li>Loads user details via {@link CustomUserDetailsService}</li>
 *   <li>Validates the token and, if valid, sets authentication into the {@link SecurityContextHolder}</li>
 *   <li>Continues the filter chain regardless of outcome</li>
 * </ol>
 *
 * <p><b>Behavior notes:</b></p>
 * <ul>
 *   <li>If no bearer token is present, the request proceeds (useful for public endpoints).</li>
 *   <li>If the token is malformed/invalid, authentication is skipped and the request proceeds.</li>
 *   <li>If authentication is already present in the security context, re-authentication is avoided.</li>
 * </ul>
 *
 * <p><b>Logging strategy:</b></p>
 * <ul>
 *   <li>Missing token and already-authenticated cases are logged at DEBUG level.</li>
 *   <li>Token parsing/validation issues are logged at WARN level.</li>
 *   <li>Successful JWT authentication is logged at INFO level (token itself is never logged).</li>
 * </ul>
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Service responsible for extracting claims and validating JWT tokens.
     */
    private final JwtService jwtService;

    /**
     * Loads user details required to validate the token and populate the security context.
     */
    private final CustomUserDetailsService userDetailsService;

    /**
     * Creates a new {@code JwtAuthenticationFilter}.
     *
     * @param jwtService          service for JWT extraction and validation
     * @param userDetailsService  service for loading user details by username
     */
    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Performs JWT-based authentication for the current request if a bearer token is present.
     *
     * @param request      current HTTP request
     * @param response     current HTTP response
     * @param filterChain  chain of filters for request processing
     * @throws ServletException if a servlet error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // No JWT provided (common for public endpoints)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("No Bearer token found | method={} | path={}", request.getMethod(), request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);

        String username;
        try {
            username = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            // Token parsing/signature issues will land here
            log.warn("Failed to extract username from token | method={} | path={}", request.getMethod(), request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        if (username == null) {
            log.warn("Token did not contain username | method={} | path={}", request.getMethod(), request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        // Avoid re-auth if context already populated
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            log.debug("Security context already has authentication | username={} | method={} | path={}",
                    username, request.getMethod(), request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        log.debug("JWT detected, attempting authentication | username={} | method={} | path={}",
                username, request.getMethod(), request.getRequestURI());

        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (jwtService.isTokenValid(jwt, userDetails)) {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            log.info("JWT authentication successful | username={} | method={} | path={}",
                    username, request.getMethod(), request.getRequestURI());
        } else {
            log.warn("Invalid JWT token | username={} | method={} | path={}",
                    username, request.getMethod(), request.getRequestURI());
        }

        filterChain.doFilter(request, response);
    }
}