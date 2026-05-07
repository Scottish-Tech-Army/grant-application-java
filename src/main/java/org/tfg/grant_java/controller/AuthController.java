package org.tfg.grant_java.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tfg.grant_java.entity.AppUser;
import org.tfg.grant_java.model.LoginRequest;
import org.tfg.grant_java.repository.AppUserRepository;
import org.tfg.grant_java.security.AuthService;

/**
 * REST controller for authentication and user registration.
 *
 * Provides endpoints for user login and registration.
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AppUserRepository repo;
    private final PasswordEncoder encoder;

    public AuthController(AuthService authService,
                          AppUserRepository repo,
                          PasswordEncoder encoder) {
        this.authService = authService;
        this.repo = repo;
        this.encoder = encoder;
    }

    /**
     * Authenticates a user and returns a JWT token.
     *
     * @param request login request containing credentials and charity scope
     * @return JWT token
     */
    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest request) {

        log.info(
                "Login attempt | username={} | charityId={}",
                request.getUsername(),
                request.getCharityId()
        );

        String token = authService.login(
                request.getUsername(),
                request.getPassword(),
                request.getCharityId()
        );

        log.info(
                "Login successful | username={} | charityId={}",
                request.getUsername(),
                request.getCharityId()
        );

        return token;
    }

    /**
     * Registers a new user in the system.
     *
     * @param user user details to register
     * @return registration confirmation message
     */
    @PostMapping("/register")
    public String register(@RequestBody AppUser user) {

        log.info(
                "User registration attempt | username={} | charityId={}",
                user.getUsername(),
                user.getCharityId()
        );

        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);

        log.info(
                "User registered successfully | username={} | charityId={}",
                user.getUsername(),
                user.getCharityId()
        );

        return "User registered.";
    }
}