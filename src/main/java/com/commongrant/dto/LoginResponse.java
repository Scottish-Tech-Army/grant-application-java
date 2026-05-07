package com.commongrant.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class LoginResponse {
    private String token;
    private String email;
    private String fullName;
    private Long orgId;
    private String orgName;
    private String role;
}

