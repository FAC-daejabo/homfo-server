package com.homfo.user.adapter;

import com.homfo.auth.dto.JwtSecretDto;
import com.homfo.auth.infra.util.JwtUtil;
import com.homfo.user.entity.JpaEmployeeRefreshToken;
import com.homfo.user.repository.EmployeeRefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;

class EmployeeRefreshTokenPersistenceAdapterTest {

    @Mock
    private EmployeeRefreshTokenRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private EmployeeRefreshTokenPersistenceAdapter adapter;

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
        when(repository.save(any(JpaEmployeeRefreshToken.class))).thenReturn(null);

        try (MockedStatic<JwtUtil> jwtUtil = Mockito.mockStatic(JwtUtil.class)) {
            jwtUtil.when(() -> JwtUtil.createToken(userId, jwtSecretDto)).thenReturn(rawToken);

            // when
            adapter.save(userId, jwtSecretDto);

            // then
            verify(encoder, times(1)).encode(anyString());
            verify(repository, times(1)).save(any(JpaEmployeeRefreshToken.class));
        }
    }


    @Test
    @DisplayName("사용자 ID로 리프레쉬 토큰 삭제")
    void deleteRefreshTokenByUserId() {
        // given
        doNothing().when(repository).deleteById(userId);

        // when
        adapter.deleteToken(userId);

        // then
        verify(repository, times(1)).deleteById(userId);
    }
}
