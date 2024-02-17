package com.hompo.user.service;

import com.hompo.auth.dto.JwtSecretDto;
import com.hompo.auth.infra.util.JwtUtil;
import com.hompo.user.entity.MySqlRefreshToken;
import com.hompo.user.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MySqlUserRefreshTokenReadServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private MySqlUserRefreshTokenReadService service;

    private final Long userId = 1L;
    private final String validToken = "validToken";
    private String encodedToken;
    private final JwtSecretDto jwtSecretDto = new JwtSecretDto("secretKey", 3600000L);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        encodedToken = new BCryptPasswordEncoder().encode(validToken);
    }

    @Test
    @DisplayName("유효한 토큰으로 조회 시 토큰 반환")
    void givenValidToken_whenGetVerifyToken_thenReturnsToken() {
        // given
        MySqlRefreshToken refreshToken = new MySqlRefreshToken(userId, encodedToken);
        when(refreshTokenRepository.findById(userId)).thenReturn(Optional.of(refreshToken));
        when(encoder.matches(validToken, encodedToken)).thenReturn(true);

        try (MockedStatic<JwtUtil> jwtUtil = Mockito.mockStatic(JwtUtil.class)) {
            MockedStatic.Verification jwtVerification = () -> JwtUtil.getUserIdFromToken(validToken, jwtSecretDto);

            jwtUtil.when(jwtVerification).thenReturn(userId);

            // when
            String result = service.getVerifyToken(validToken, jwtSecretDto);

            // then
            assertThat(result).isEqualTo(validToken);
            verify(encoder, times(1)).matches(validToken, refreshToken.getToken());
            jwtUtil.verify(jwtVerification, times(1));
        }
    }

    @Test
    @DisplayName("토큰 불일치 시 예외 발생")
    void givenMismatchToken_whenGetVerifyToken_thenThrowsException() {
        MySqlRefreshToken refreshToken = new MySqlRefreshToken(userId, encodedToken);
        when(refreshTokenRepository.findById(userId)).thenReturn(Optional.of(refreshToken));
        when(encoder.matches(validToken, encodedToken)).thenReturn(false);

        try (MockedStatic<JwtUtil> jwtUtil = Mockito.mockStatic(JwtUtil.class)) {
            MockedStatic.Verification jwtVerification = () -> JwtUtil.getUserIdFromToken(validToken, jwtSecretDto);

            jwtUtil.when(jwtVerification).thenReturn(userId);

            // when
            Executable result = () -> service.getVerifyToken(validToken, jwtSecretDto);

            // then
            assertThrows(RuntimeException.class, result);
            verify(refreshTokenRepository, times(1)).findById(userId);
            verify(encoder, times(1)).matches(validToken, refreshToken.getToken());
            jwtUtil.verify(jwtVerification, times(1));
        }
    }

    @Test
    @DisplayName("토큰이 존재하지 않을 경우 예외 발생")
    void givenNoToken_whenGetVerifyToken_thenThrowsException() {
        // given
        when(refreshTokenRepository.findById(userId)).thenReturn(Optional.empty());

        // when
        Executable result = () -> service.getVerifyToken(validToken, jwtSecretDto);

        // then
        assertThrows(RuntimeException.class, result);
    }
}
