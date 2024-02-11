package com.hompo.auth.infra.util;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.hompo.auth.dto.JwtSecretDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtSecretDto jwtSecretDto;
    private Long userId;
    private String secretKey;
    private long expireTime;

    @BeforeEach
    void setUp() {
        secretKey = "testSecretKey";
        expireTime = 60000L; // 60 seconds
        jwtSecretDto = new JwtSecretDto(secretKey, expireTime);
        userId = 1L;
    }

    @Test
    void createToken_WithValidUserId_ShouldReturnToken() {
        // given

        // when
        String token = JwtUtil.createToken(userId, jwtSecretDto);

        // then
        assertNotNull(token, "토큰은 Null이면 안 됩니다.");
    }

    @Test
    void verifyToken_WithValidToken_ShouldReturnTrue() {
        // given
        String token = JwtUtil.createToken(userId, jwtSecretDto);

        // when
        boolean result = assertDoesNotThrow(() -> JwtUtil.verifyToken(token, jwtSecretDto), "verifyToken은 Exception이 발생하면 안 됩니다.");

        // then
        assertTrue(result, "토큰이 정상적으로 인증되었습니다.");
    }

    @Test
    void verifyTokenNotExpired_WithValidToken_ShouldReturnTrue() {
        // given
        String token = JwtUtil.createToken(userId, jwtSecretDto);

        // when
        boolean result = assertDoesNotThrow(() -> JwtUtil.verifyTokenNotExpired(token, jwtSecretDto),
                "verifyTokenNotExpired는 Exception이 발생하면 안 됩니다.");

        // then
        assertTrue(result, "Token은 아직 만료되면 안 됩니다.");
    }

    @Test
    void getUserIdFromToken_WithValidToken_ShouldReturnUserId() {
        // given
        String token = JwtUtil.createToken(userId, jwtSecretDto);

        // when
        Long extractedUserId = assertDoesNotThrow(() -> JwtUtil.getUserIdFromToken(token, jwtSecretDto),
                "getUserIdFromToken는 Exception이 발생하면 안 됩니다.");

        // then
        assertEquals(userId, extractedUserId, "추출된 userId는 원본과 같아야 합니다.");
    }

    @Test
    void verifyToken_WithInvalidSignature_ShouldReturnFalse() {
        // given
        String token = JwtUtil.createToken(userId, jwtSecretDto);
        JwtSecretDto wrongJwtSecretDto = new JwtSecretDto("wrongSecretKey", expireTime);

        // when
        boolean result = assertDoesNotThrow(() -> JwtUtil.verifyToken(token, wrongJwtSecretDto),
                "verifyTokenNotExpired는 Exception이 발생하면 안 됩니다.");

        // then
        assertFalse(result, "잘못된 정보로 인증한 토큰은 결과가 false입니다.");
    }

    @Test
    void verifyTokenNotExpired_WithExpiredToken_ShouldThrowException() {
        // given
        JwtSecretDto expiredJwtSecretDto = new JwtSecretDto(secretKey, -1000L); // Already expired
        String expiredToken = JwtUtil.createToken(userId, expiredJwtSecretDto);

        boolean result = assertDoesNotThrow(() -> JwtUtil.verifyTokenNotExpired(expiredToken, jwtSecretDto),
                "verifyTokenNotExpired는 Exception이 발생하면 안 됩니다.");

        // then
        assertFalse(result, "만료된 토큰은 결과가 false입니다.");
    }
}
