package org.tfg.grant_java.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.tfg.grant_java.exception.ServiceException;
import org.tfg.grant_java.model.Constants;
import org.tfg.grant_java.model.CustomUserDetails;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private Authentication authentication;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(authenticationManager, jwtService);
    }

    @Test
    void login_shouldReturnToken_whenAuthenticated_andCharityMatches() {
        // Arrange
        String username = "testUser";
        String password = "testPassword";
        Long charityId = 1L;

        CustomUserDetails principal = new CustomUserDetails(
                username,
                password,
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                charityId
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(principal);

        when(jwtService.generateToken(principal)).thenReturn("jwt-token-123");

        // Act
        String token = authService.login(username, password, charityId);

        // Assert
        assertThat(token).isEqualTo("jwt-token-123");

        // verify token generation called with same principal
        verify(jwtService, times(1)).generateToken(principal);

        // verify authenticate called with correct UsernamePasswordAuthenticationToken
        ArgumentCaptor<UsernamePasswordAuthenticationToken> captor =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager).authenticate(captor.capture());

        UsernamePasswordAuthenticationToken authToken = captor.getValue();
        assertThat(authToken.getPrincipal()).isEqualTo(username);
        assertThat(authToken.getCredentials()).isEqualTo(password);

        verify(authentication).isAuthenticated();
        verify(authentication).getPrincipal();
        verifyNoMoreInteractions(jwtService, authenticationManager, authentication);
    }

    @Test
    void login_shouldThrowRuntimeException_whenNotAuthenticated() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(authentication.isAuthenticated()).thenReturn(false);

        // Act + Assert
        assertThatThrownBy(() -> authService.login("u", "p", 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid login");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authentication).isAuthenticated();

        // Should not proceed to principal or jwt generation
        verify(authentication, never()).getPrincipal();
        verifyNoInteractions(jwtService);
    }

    @Test
    void login_shouldThrowServiceException_whenCharityIdMismatch() {
        // Arrange
        Long actualCharityIdOnUser = 99L;
        Long requestedCharityId = 1L;

        CustomUserDetails principal = new CustomUserDetails(
                "testUser",
                "testPassword",
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                actualCharityIdOnUser
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(principal);

        // Act + Assert
        assertThatThrownBy(() -> authService.login("testUser", "testPassword", requestedCharityId))
                .isInstanceOf(ServiceException.class)
                .extracting(ex -> ((ServiceException) ex).getErrorCode())
                .isEqualTo(Constants.USER_NOT_PRESENT);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authentication).isAuthenticated();
        verify(authentication).getPrincipal();
        verifyNoInteractions(jwtService);
    }
}