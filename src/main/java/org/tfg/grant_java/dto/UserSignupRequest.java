package org.tfg.grant_java.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User signup request")
public class UserSignupRequest {

    @Schema(example = "test.user@gmail.com")
    private String email;

    @Schema(example = "PlainTextPassword123")
    private String password;

    @Schema(example = "Smith")
    private String firstName;

    @Schema(example = "Jackson")
    private String lastName;
}
