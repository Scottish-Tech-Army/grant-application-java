package org.tfg.grant_java.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tfg.grant_java.dto.AppUserDTO;
import org.tfg.grant_java.dto.CreateUserRequest;
import org.tfg.grant_java.entity.AppUser;
import org.tfg.grant_java.exception.ServiceException;
import org.tfg.grant_java.model.Constants;
import org.tfg.grant_java.repository.AppUserRepository;
import org.tfg.grant_java.service.AppUserService;

import java.util.List;

/**
 * Default implementation of {@link AppUserService}.
 *
 * <p>This service handles creation and retrieval of application users and
 * enforces basic business rules around user existence and charity scoping.</p>
 *
 * <p><b>Main responsibilities:</b></p>
 * <ul>
 *   <li>Create new users for a given charity</li>
 *   <li>Retrieve users by identifier</li>
 *   <li>List users associated with a specific charity</li>
 * </ul>
 *
 * <p><b>Security notes:</b></p>
 * <ul>
 *   <li>Passwords are assumed to be hashed <b>before</b> reaching this service</li>
 *   <li>Only active users are created by default</li>
 * </ul>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    /**
     * Repository used for CRUD operations on {@link AppUser} entities.
     */
    private final AppUserRepository repository;

    /**
     * Creates a new application user.
     *
     * <p>This method:</p>
     * <ol>
     *   <li>Maps the incoming {@link CreateUserRequest} to an {@link AppUser} entity</li>
     *   <li>Persists the user</li>
     *   <li>Returns a mapped {@link AppUserDTO}</li>
     * </ol>
     *
     * @param request request containing user creation details
     * @return created user as {@link AppUserDTO}
     */
    @Override
    public AppUserDTO createUser(CreateUserRequest request) {

        log.info(
                "Creating user | username={} | role={} | charityId={}",
                request.getUsername(),
                request.getRole(),
                request.getCharityId()
        );

        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // hashed earlier in controller
        user.setRole(AppUser.Role.valueOf(request.getRole()));
        user.setCharityId(request.getCharityId());
        user.setIsActive(true);
        user.setCreatedBy(request.getUsername());

        AppUser savedUser = repository.save(user);

        log.info(
                "User created | userId={} | username={} | charityId={}",
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getCharityId()
        );

        return mapToDto(savedUser);
    }

    /**
     * Retrieves a user by its unique identifier.
     *
     * @param id unique identifier of the user
     * @return user data transfer object
     * @throws ServiceException if the user does not exist
     */
    @Override
    public AppUserDTO getUserById(Long id) {

        log.info("Fetching user | userId={}", id);

        return repository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> {
                    log.warn("User not found | userId={}", id);
                    return new ServiceException(Constants.COMMON_DATA_ID_NOT_FOUND);
                });
    }

    /**
     * Retrieves all users associated with a specific charity.
     *
     * <p>This method filters users in-memory based on {@code charityId}.</p>
     *
     * @param charityId identifier of the charity
     * @return list of users belonging to the specified charity
     */
    @Override
    public List<AppUserDTO> getUsersByCharity(Long charityId) {

        log.info("Fetching users by charity | charityId={}", charityId);

        List<AppUserDTO> users = repository.findAll().stream()
                .filter(u -> charityId.equals(u.getCharityId()))
                .map(this::mapToDto)
                .toList();

        log.info(
                "Users fetched by charity | charityId={} | count={}",
                charityId,
                users.size()
        );

        return users;
    }

    /**
     * Maps an {@link AppUser} entity to {@link AppUserDTO}.
     *
     * @param user application user entity
     * @return mapped DTO
     */
    private AppUserDTO mapToDto(AppUser user) {

        AppUserDTO dto = new AppUserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole().name());
        dto.setCharityId(user.getCharityId());
        dto.setIsActive(user.getIsActive());
        dto.setCreatedBy(user.getCreatedBy());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}