package org.tfg.grant_java.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.tfg.grant_java.model.CustomUserDetails;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    void generateToken_shouldContainUsernameAndCharityId_andBeValid() {
        // Arrange
        CustomUserDetails user = new CustomUserDetails(
                "testUser",
                "pw",
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                10L
        );

        // Act
        String token = jwtService.generateToken(user);

        // Assert: token not null/empty
        assertThat(token).isNotBlank();

        // Assert: can extract username and charityId from token
        assertThat(jwtService.extractUsername(token)).isEqualTo("testUser");
        assertThat(jwtService.extractCharityId(token)).isEqualTo(10L);

        // Assert: token considered valid for same user
        assertThat(jwtService.isTokenValid(token, user)).isTrue();
    }

    @Test
    void isTokenValid_shouldReturnFalse_whenUsernameMismatch() {
        // Arrange
        CustomUserDetails user1 = new CustomUserDetails(
                "user1", "pw",
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                10L
        );
        CustomUserDetails user2 = new CustomUserDetails(
                "user2", "pw",
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                10L
        );

        String token = jwtService.generateToken(user1);

        // Act + Assert
        assertThat(jwtService.isTokenValid(token, user2)).isFalse();
    }

    @Test
    void isTokenValid_shouldReturnFalse_whenCharityIdMismatch() {
        // Arrange
        CustomUserDetails user1 = new CustomUserDetails(
                "testUser", "pw",
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                10L
        );
        CustomUserDetails user2 = new CustomUserDetails(
                "testUser", "pw",
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                99L
        );

        String token = jwtService.generateToken(user1);

        // Act + Assert
        assertThat(jwtService.isTokenValid(token, user2)).isFalse();
    }

    @Test
    void extractUsername_shouldThrowException_whenTokenIsTampered() {
        // Arrange
        CustomUserDetails user = new CustomUserDetails(
                "testUser", "pw",
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                10L
        );

        String token = jwtService.generateToken(user);

        // Tamper token by changing one character (breaks signature)
        String tampered = token.substring(0, token.length() - 2) + "aa";

        // Act + Assert
        assertThatThrownBy(() -> jwtService.extractUsername(tampered))
                .isInstanceOf(Exception.class); // JJWT throws a runtime exception for invalid signature
    }
}