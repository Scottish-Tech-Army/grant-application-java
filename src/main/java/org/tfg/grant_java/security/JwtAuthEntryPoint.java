package org.tfg.grant_java.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * JWT authentication entry point that handles unauthorized access attempts.
 *
 * <p>This component is invoked by Spring Security when an unauthenticated
 * user attempts to access a protected resource. It is responsible for
 * returning an appropriate HTTP 401 (Unauthorized) response.</p>
 *
 * <p><b>Key responsibilities:</b></p>
 * <ul>
 *   <li>Intercepts authentication failures before controller execution</li>
 *   <li>Logs unauthorized access attempts with request context</li>
 *   <li>Sends a standardized HTTP 401 response to the client</li>
 * </ul>
 *
 * <p><b>Logging strategy:</b></p>
 * <ul>
 *   <li>Unauthorized access attempts are logged at <b>WARN</b> level</li>
 *   <li>Includes HTTP method, request path, and failure reason</li>
 * </ul>
 */
@Slf4j
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    /**
     * Commences an authentication scheme.
     *
     * <p>This method is called when an unauthenticated request tries to access
     * a secured endpoint. It responds with HTTP 401 without exposing
     * sensitive details.</p>
     *
     * @param request       the HTTP request that resulted in an authentication failure
     * @param response      the HTTP response to be sent to the client
     * @param authException the authentication exception that triggered this entry point
     * @throws IOException if an I/O error occurs while sending the response
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        log.warn(
                "Unauthorized access attempt | method={} | path={} | reason={}",
                request.getMethod(),
                request.getRequestURI(),
                authException.getMessage()
        );

        response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized"
        );
    }
}