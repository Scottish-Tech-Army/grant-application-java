package org.tfg.grant_java.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.AuthenticationException;

import static org.mockito.Mockito.*;

class JwtAuthEntryPointTest {

    @Test
    void commence_shouldSend401Unauthorized() throws Exception {
        // Arrange
        JwtAuthEntryPoint entryPoint = new JwtAuthEntryPoint();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AuthenticationException authException = mock(AuthenticationException.class);

        // Act
        entryPoint.commence(request, response, authException);

        // Assert
        verify(response, times(1))
                .sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");

        verifyNoMoreInteractions(response);
    }
}