package com.commongrant.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class RegisterRequest {
    @NotBlank private String email;
    @NotBlank private String password;
    @NotBlank private String fullName;
    @NotBlank private String orgName;
    private String ein;
    private String city;
    private String state;
}

