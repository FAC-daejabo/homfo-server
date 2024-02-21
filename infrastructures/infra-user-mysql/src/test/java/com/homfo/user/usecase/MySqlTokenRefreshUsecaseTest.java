package com.homfo.user.usecase;

import com.homfo.auth.command.TokenRefreshCommand;
import com.homfo.auth.dto.JwtDto;
import com.homfo.auth.dto.JwtSecretDto;
import com.homfo.auth.infra.util.JwtUtil;
import com.homfo.user.service.UserRefreshTokenReadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MySqlTokenRefreshUsecaseTest {

    @Mock
    private UserRefreshTokenReadService userRefreshTokenReadService;

    @Mock(name = "userAccessTokenInfo")
    private JwtSecretDto userAccessTokenInfo;

    @Mock(name = "userRefreshTokenInfo")
    private JwtSecretDto userRefreshTokenInfo;

    private MySqlTokenRefreshUsecase tokenRefreshUsecase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        tokenRefreshUsecase = new MySqlTokenRefreshUsecase(userRefreshTokenReadService, userAccessTokenInfo, userRefreshTokenInfo);
    }

    @Test
    @DisplayName("토큰 갱신 시 새로운 JWT 정보를 반환한다")
    void givenUserIdAndRefreshToken_whenExecute_thenReturnsNewJwtDto() {
        // Given
        long userId = 1L;
        TokenRefreshCommand command = new TokenRefreshCommand("refreshToken");
        String expectedRefreshToken = command.token();
        String expectedAccessToken = "newAccessToken";

        when(userRefreshTokenReadService.getVerifyToken(command.token(), userRefreshTokenInfo)).thenReturn(expectedRefreshToken);

        try (MockedStatic<JwtUtil> jwtUtil = Mockito.mockStatic(JwtUtil.class)) {
            jwtUtil.when(() -> JwtUtil.createToken(eq(userId), any(JwtSecretDto.class))).thenReturn(expectedAccessToken);

            // When
            JwtDto result = tokenRefreshUsecase.execute(userId, command);

            // Then
            assertThat(result.accessToken()).isEqualTo(expectedAccessToken);
            assertThat(result.refreshToken()).isEqualTo(expectedRefreshToken);
            verify(userRefreshTokenReadService, times(1)).getVerifyToken(command.token(), userRefreshTokenInfo);
        }
    }
}
