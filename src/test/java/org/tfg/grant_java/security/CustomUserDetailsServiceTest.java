package org.tfg.grant_java.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.tfg.grant_java.entity.AppUser;
import org.tfg.grant_java.model.CustomUserDetails;
import org.tfg.grant_java.repository.AppUserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private AppUserRepository repo;

    private CustomUserDetailsService service;

    @BeforeEach
    void setUp() {
        service = new CustomUserDetailsService(repo);
    }

    @Test
    void loadUserByUsername_shouldReturnCustomUserDetails_whenActiveUserExists() {
        // Arrange
        AppUser user = new AppUser();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setRole(AppUser.Role.USER);
        user.setCharityId(10L);
        user.setIsActive(true);

        when(repo.findByUsernameAndIsActiveTrue("testUser"))
                .thenReturn(Optional.of(user));

        // Act
        CustomUserDetails details = service.loadUserByUsername("testUser");

        // Assert
        assertThat(details).isNotNull();
        assertThat(details.getUsername()).isEqualTo("testUser");
        assertThat(details.getPassword()).isEqualTo("testPassword");
        assertThat(details.getCharityId()).isEqualTo(10L);

        // Assert authority = "ROLE_" + role
        assertThat(details.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_USER");

        // Verify repository call
        verify(repo, times(1)).findByUsernameAndIsActiveTrue("testUser");
        verifyNoMoreInteractions(repo);
    }

    @Test
    void loadUserByUsername_shouldReturnRoleAdminAuthority_whenRoleIsAdmin() {
        // Arrange
        AppUser user = new AppUser();
        user.setUsername("admin");
        user.setPassword("pw");
        user.setRole(AppUser.Role.ADMIN);
        user.setCharityId(99L);
        user.setIsActive(true);

        when(repo.findByUsernameAndIsActiveTrue("admin"))
                .thenReturn(Optional.of(user));

        // Act
        CustomUserDetails details = service.loadUserByUsername("admin");

        // Assert
        assertThat(details.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_ADMIN");
        assertThat(details.getCharityId()).isEqualTo(99L);

        verify(repo).findByUsernameAndIsActiveTrue("admin");
    }

    @Test
    void loadUserByUsername_shouldThrowUsernameNotFoundException_whenUserNotFound() {
        // Arrange
        when(repo.findByUsernameAndIsActiveTrue("missing"))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> service.loadUserByUsername("missing"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("1006");

        verify(repo, times(1)).findByUsernameAndIsActiveTrue("missing");
        verifyNoMoreInteractions(repo);
    }

    @Test
    void loadUserByUsername_shouldPassSameUsernameToRepository() {
        // Arrange
        AppUser user = new AppUser();
        user.setUsername("someUser");
        user.setPassword("pw");
        user.setRole(AppUser.Role.USER);
        user.setCharityId(1L);
        user.setIsActive(true);

        when(repo.findByUsernameAndIsActiveTrue(anyString()))
                .thenReturn(Optional.of(user));

        // Act
        service.loadUserByUsername("someUser");

        // Assert repo called with exact username
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(repo).findByUsernameAndIsActiveTrue(captor.capture());
        assertThat(captor.getValue()).isEqualTo("someUser");
    }
}