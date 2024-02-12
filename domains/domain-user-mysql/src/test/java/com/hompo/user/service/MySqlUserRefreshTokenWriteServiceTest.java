package com.hompo.user.service;

import com.hompo.auth.dto.JwtSecretDto;
import com.hompo.auth.infra.util.JwtUtil;
import com.hompo.user.entity.MySqlRefreshToken;
import com.hompo.user.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;

class MySqlUserRefreshTokenWriteServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private MySqlUserRefreshTokenWriteService service;

    private final long userId = 1L;
    private final JwtSecretDto jwtSecretDto = new JwtSecretDto("secretKey", 3600000L);
    private final String rawToken = "rawToken";
    private String encodedToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(encoder.encode(rawToken)).thenReturn(new BCryptPasswordEncoder().encode(rawToken));
    }

    @Test
    @DisplayName("새로운 리프레쉬 토큰 저장")
    void saveRefreshToken() {
        // given
        when(refreshTokenRepository.save(any(MySqlRefreshToken.class))).thenReturn(null);

        try (MockedStatic<JwtUtil> jwtUtil = Mockito.mockStatic(JwtUtil.class)) {
            jwtUtil.when(() -> JwtUtil.createToken(userId, jwtSecretDto)).thenReturn(rawToken);

            // when
            service.save(userId, jwtSecretDto);

            // then
            verify(encoder, times(1)).encode(anyString());
            verify(refreshTokenRepository, times(1)).save(any(MySqlRefreshToken.class));
        }
    }


    @Test
    @DisplayName("사용자 ID로 리프레쉬 토큰 삭제")
    void deleteRefreshTokenByUserId() {
        // given
        doNothing().when(refreshTokenRepository).deleteById(userId);

        // when
        service.deleteByUserId(userId);

        // then
        verify(refreshTokenRepository, times(1)).deleteById(userId);
    }
}
