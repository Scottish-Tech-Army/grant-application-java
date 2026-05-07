package org.tfg.grant_java.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User login request")
public class UserLoginRequest {

    @Schema(example = "test.user@gmail.com")
    private String email;

    @Schema(example = "PlainTextPassword123")
    private String password;
}
