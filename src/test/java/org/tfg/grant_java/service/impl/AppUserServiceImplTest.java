package org.tfg.grant_java.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tfg.grant_java.dto.AppUserDTO;
import org.tfg.grant_java.dto.CreateUserRequest;
import org.tfg.grant_java.entity.AppUser;
import org.tfg.grant_java.exception.ServiceException;
import org.tfg.grant_java.model.Constants;
import org.tfg.grant_java.repository.AppUserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserServiceImplTest {

    @Mock
    private AppUserRepository repository;

    @InjectMocks
    private AppUserServiceImpl service;

    @Test
    void createUser_shouldSaveUserAndReturnDto() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testUser");
        request.setPassword("testPassword");
        request.setRole("USER");     // must match AppUser.Role enum name
        request.setCharityId(10L);

        // Let repository.save assign an id (simulate DB behavior)
        when(repository.save(any(AppUser.class))).thenAnswer(inv -> {
            AppUser u = inv.getArgument(0);
            u.setId(100L);
            return u;
        });

        // Act
        AppUserDTO dto = service.createUser(request);

        // Assert DTO mapping
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(100L);
        assertThat(dto.getUsername()).isEqualTo("testUser");
        assertThat(dto.getRole()).isEqualTo("USER");
        assertThat(dto.getCharityId()).isEqualTo(10L);
        assertThat(dto.getIsActive()).isTrue();

        // Assert the entity passed to save()
        ArgumentCaptor<AppUser> captor = ArgumentCaptor.forClass(AppUser.class);
        verify(repository).save(captor.capture());

        AppUser saved = captor.getValue();
        assertThat(saved.getUsername()).isEqualTo("testUser");
        assertThat(saved.getPassword()).isEqualTo("testPassword"); // "hash later" as per code
        assertThat(saved.getRole()).isEqualTo(AppUser.Role.USER);
        assertThat(saved.getCharityId()).isEqualTo(10L);
        assertThat(saved.getIsActive()).isTrue();

        verifyNoMoreInteractions(repository);
    }

    @Test
    void getUserById_shouldReturnDto_whenUserExists() {
        // Arrange
        AppUser user = new AppUser();
        user.setId(1L);
        user.setUsername("john");
        user.setPassword("pw");
        user.setRole(AppUser.Role.ADMIN);
        user.setCharityId(99L);
        user.setIsActive(true);

        when(repository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        AppUserDTO dto = service.getUserById(1L);

        // Assert
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getUsername()).isEqualTo("john");
        assertThat(dto.getRole()).isEqualTo("ADMIN");
        assertThat(dto.getCharityId()).isEqualTo(99L);
        assertThat(dto.getIsActive()).isTrue();

        verify(repository).findById(1L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void getUserById_shouldThrowServiceException_whenUserNotFound() {
        // Arrange
        when(repository.findById(404L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> service.getUserById(404L))
                .isInstanceOf(ServiceException.class)
                .extracting(ex -> ((ServiceException) ex).getErrorCode())
                // matches your current implementation exactly
                .isEqualTo(Constants.COMMON_DATA_ID_NOT_FOUND);

        verify(repository).findById(404L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void getUsersByCharity_shouldFilterUsersByCharityId_andMapToDtos() {
        // Arrange: 3 users total, only 2 match charityId=10
        AppUser u1 = new AppUser();
        u1.setId(1L);
        u1.setUsername("u1");
        u1.setRole(AppUser.Role.USER);
        u1.setCharityId(10L);
        u1.setIsActive(true);

        AppUser u2 = new AppUser();
        u2.setId(2L);
        u2.setUsername("u2");
        u2.setRole(AppUser.Role.ADMIN);
        u2.setCharityId(10L);
        u2.setIsActive(false);

        AppUser u3 = new AppUser();
        u3.setId(3L);
        u3.setUsername("u3");
        u3.setRole(AppUser.Role.USER);
        u3.setCharityId(20L);
        u3.setIsActive(true);

        when(repository.findAll()).thenReturn(List.of(u1, u2, u3));

        // Act
        List<AppUserDTO> result = service.getUsersByCharity(10L);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(d -> d.getCharityId().equals(10L));
        assertThat(result).extracting(AppUserDTO::getUsername)
                .containsExactlyInAnyOrder("u1", "u2");
        assertThat(result).extracting(AppUserDTO::getRole)
                .containsExactlyInAnyOrder("USER", "ADMIN");
        assertThat(result).extracting(AppUserDTO::getIsActive)
                .containsExactlyInAnyOrder(true, false);

        verify(repository).findAll();
        verifyNoMoreInteractions(repository);
    }
}