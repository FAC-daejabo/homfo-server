package com.hompo.user.usecase;

import com.hompo.auth.dto.JwtDto;
import com.hompo.auth.dto.JwtSecretDto;
import com.hompo.auth.infra.util.JwtUtil;
import com.hompo.enums.Gender;
import com.hompo.user.command.SignInCommand;
import com.hompo.user.dto.UserDto;
import com.hompo.user.infra.enums.UserStatus;
import com.hompo.user.service.UserReadService;
import com.hompo.user.service.UserRefreshTokenWriteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MySqlSignInUsecaseTest {

    @Mock
    private UserReadService userReadService;

    @Mock
    private UserRefreshTokenWriteService userRefreshTokenWriteService;

    @Mock(name = "userAccessTokenInfo")
    private JwtSecretDto userAccessTokenInfo;

    @Mock(name = "userRefreshTokenInfo")
    private JwtSecretDto userRefreshTokenInfo;

    // @InjectMocks
    // 아무리해도 userAccessTokenInfo랑 userRefreshTokenInfo가 분리돼서 주입되지 않고 둘 중 하나만 자꾸 주입된다...
    private MySqlSignInUsecase signInUsecase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        signInUsecase = new MySqlSignInUsecase(userReadService, userRefreshTokenWriteService, userAccessTokenInfo, userRefreshTokenInfo);
    }

    @Test
    @DisplayName("로그인 시 JWT 정보를 반환한다")
    void givenSignInCommand_whenExecute_thenReturnsJwtDto() {
        // Given
        long userId = 1L;
        SignInCommand command = new SignInCommand("testUser", "password");
        UserDto userDto = new UserDto(userId, "testUser", "testUser", "999-9999-9999", Gender.MAN, "job", LocalDate.now(), UserStatus.USE);
        String expectedAccessToken = "accessToken";
        String expectedRefreshToken = "refreshToken";

        when(userReadService.signIn(command)).thenReturn(userDto);
        when(userRefreshTokenWriteService.save(userDto.id(), userRefreshTokenInfo)).thenReturn(expectedRefreshToken);
        
        try (MockedStatic<JwtUtil> jwtUtil = Mockito.mockStatic(JwtUtil.class)) {
            jwtUtil.when(() -> JwtUtil.createToken(eq(userDto.id()), eq(userAccessTokenInfo))).thenReturn(expectedAccessToken);
            
            // When
            JwtDto result = signInUsecase.execute(command);

            // Then
            assertThat(result.accessToken()).isEqualTo(expectedAccessToken);
            assertThat(result.refreshToken()).isEqualTo(expectedRefreshToken);
            verify(userReadService, times(1)).signIn(command);
            verify(userRefreshTokenWriteService, times(1)).save(userDto.id(), userRefreshTokenInfo);
        }
    }
}
