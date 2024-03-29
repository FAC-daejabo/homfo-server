package com.homfo.auth.entity;

import com.homfo.auth.dto.JwtSecretDto;
import com.homfo.auth.infra.util.JwtUtil;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtTest {
    private final String secretKey = "secretKey";

    private final int expireTime = 500;

    @Test
    @DisplayName("올바른 토큰은 검증을 통과해야 한다.")
    void validateToken_WithValidToken_ShouldPass() {
        // given
        Long userId = 1L;
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        JwtSecretDto jwtSecretDto = new JwtSecretDto(secretKey, expireTime);
        String token = JwtUtil.createToken(userId, jwtSecretDto);
        String encryptedToken = encoder.encode(token);
        MockJwt jwt = new MockJwt(userId, encryptedToken);

        // when
        Executable validation = () -> jwt.validateToken(encryptedToken);

        // then
        assertDoesNotThrow(validation);
    }

    @Test
    @DisplayName("올바르지 않은 토큰은 IllegalArgumentException이 발생해야 한다.")
    void validateToken_WithInvalidToken_ShouldThrowException() {
        // given
        Long userId = 1L;
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        JwtSecretDto jwtSecretDto = new JwtSecretDto(secretKey, expireTime);
        String token = JwtUtil.createToken(userId, jwtSecretDto);
        String encryptedToken = encoder.encode(token);
        MockJwt jwt = new MockJwt(userId, encryptedToken);

        // when
        Executable validation = () -> jwt.validateToken(token);

        // then
        assertThrows(IllegalArgumentException.class, validation);
    }
}
