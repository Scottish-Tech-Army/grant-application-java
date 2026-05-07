package org.tfg.grant_java.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tfg.grant_java.dto.UserLoginRequest;
import org.tfg.grant_java.dto.UserSignupRequest;
import org.tfg.grant_java.entity.UserEntity;
import org.tfg.grant_java.repository.UserRepository;

import java.time.LocalDate;

@Slf4j
@Service
public class UserService {


    private final UserRepository userRepo;

    @Autowired
    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }
    //private final UserTemplateService templateService;

    public UserEntity signup(UserSignupRequest request) {

        UserEntity userEntity = UserEntity.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .passwordHash(request.getPassword())
                .active(true)
                .createdDate(LocalDate.now().toString())
                .build();


        UserEntity saved = userRepo.save(userEntity);

        // create predefined templates
        // templateService.seedUserTemplatesIfMissing(saved.getId());

        return saved;
    }


    public UserEntity login(UserLoginRequest request) {

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new RuntimeException("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new RuntimeException("Password is required");
        }

        UserEntity user = userRepo.findByEmailIgnoreCase(request.getEmail().trim())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (Boolean.FALSE.equals(user.getActive())) {
            throw new RuntimeException("User is inactive");
        }

        boolean ok = request.getPassword().equals(user.getPasswordHash());
        if (!ok) {
            throw new RuntimeException("Invalid credentials");
        }

        log.info("User login success | userId={} email={}", user.getId(), user.getEmail());
        return user;
    }

}
