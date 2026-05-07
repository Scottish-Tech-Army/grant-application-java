package org.tfg.grant_java.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tfg.grant_java.dto.UserLoginRequest;
import org.tfg.grant_java.dto.UserResponse;
import org.tfg.grant_java.dto.UserSignupRequest;
import org.tfg.grant_java.entity.UserEntity;
import org.tfg.grant_java.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "User signup and login APIs")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "User signup",
            description = "Registers a new user and stores hashed password"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Email already exists")
    })
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(
            @RequestBody UserSignupRequest request) {

        UserEntity user = userService.signup(request);

        return ResponseEntity.ok(
                UserResponse.builder()
                        .userId(user.getId())
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .active(user.getActive())
                        .build()
        );
    }

    @Operation(
            summary = "User login",
            description = "Validates email and password and returns user details"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(
            @RequestBody UserLoginRequest request) {

        UserEntity user = userService.login(request);

        return ResponseEntity.ok(
                UserResponse.builder()
                        .userId(user.getId())
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .active(user.getActive())
                        .build()
        );
    }
}
