package org.tfg.grant_java.security;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.tfg.grant_java.model.CustomUserDetails;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    private JwtService jwtService;
    private CustomUserDetailsService userDetailsService;
    private JwtAuthenticationFilter filter;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        jwtService = mock(JwtService.class);
        userDetailsService = mock(CustomUserDetailsService.class);
        filter = new JwtAuthenticationFilter(jwtService, userDetailsService);
        filterChain = mock(FilterChain.class);

        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldContinueChain_whenNoAuthorizationHeader() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verifyNoInteractions(jwtService, userDetailsService);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void shouldContinueChain_whenAuthorizationHeaderNotBearer() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Basic abc.def.ghi"); // not Bearer
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService, userDetailsService);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void shouldSetAuthentication_whenBearerTokenValid_andNoExistingAuth() throws Exception {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token123");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtService.extractUsername("token123")).thenReturn("testUser");

        CustomUserDetails userDetails = new CustomUserDetails(
                "testUser",
                "pw",
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                10L
        );

        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);
        when(jwtService.isTokenValid("token123", userDetails)).thenReturn(true);

        // Act
        filter.doFilter(request, response, filterChain);

        // Assert: auth is set
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication())
                .isInstanceOf(UsernamePasswordAuthenticationToken.class);

        UsernamePasswordAuthenticationToken auth =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        assertThat(auth.getPrincipal()).isEqualTo(userDetails);
        assertThat(auth.getAuthorities())
                .extracting(a -> a.getAuthority())
                .containsExactly("ROLE_USER");

        // verify chain continues
        verify(filterChain).doFilter(request, response);

        // verify collaborators called
        verify(jwtService).extractUsername("token123");
        verify(userDetailsService).loadUserByUsername("testUser");
        verify(jwtService).isTokenValid("token123", userDetails);
    }

    @Test
    void shouldNotSetAuthentication_whenBearerTokenInvalid() throws Exception {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token123");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtService.extractUsername("token123")).thenReturn("testUser");

        CustomUserDetails userDetails = new CustomUserDetails(
                "testUser",
                "pw",
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                10L
        );

        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);
        when(jwtService.isTokenValid("token123", userDetails)).thenReturn(false);

        // Act
        filter.doFilter(request, response, filterChain);

        // Assert
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);

        verify(jwtService).extractUsername("token123");
        verify(userDetailsService).loadUserByUsername("testUser");
        verify(jwtService).isTokenValid("token123", userDetails);
    }

    @Test
    void shouldNotReAuthenticate_whenSecurityContextAlreadyHasAuthentication() throws Exception {
        // Arrange an existing authentication in context
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("already", null, List.of())
        );

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token123");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtService.extractUsername("token123")).thenReturn("testUser"); // may still be called
        // But because existing auth is not null, filter should NOT load user / validate / set new auth

        // Act
        filter.doFilter(request, response, filterChain);

        // Assert: authentication remains unchanged
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).isEqualTo("already");

        verify(filterChain).doFilter(request, response);

        // In your code, extractUsername is called before the "auth == null" check, so it WILL be called.
        verify(jwtService).extractUsername("token123");

        // But these should NOT be called
        verifyNoInteractions(userDetailsService);
        verify(jwtService, never()).isTokenValid(anyString(), any());
    }
}