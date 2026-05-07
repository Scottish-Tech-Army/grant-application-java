package org.tfg.grant_java.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User response")
public class UserResponse {

    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean active;
}
