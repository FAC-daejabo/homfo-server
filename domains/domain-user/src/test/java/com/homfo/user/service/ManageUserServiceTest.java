package com.homfo.user.service;

import com.homfo.auth.command.TokenRefreshCommand;
import com.homfo.auth.dto.JwtDto;
import com.homfo.auth.dto.JwtSecretDto;
import com.homfo.auth.infra.util.JwtUtil;
import com.homfo.auth.port.LoadJwtPort;
import com.homfo.auth.port.ManageJwtPort;
import com.homfo.enums.Gender;
import com.homfo.enums.MarketingCode;
import com.homfo.user.command.RegisterCommand;
import com.homfo.user.command.SignInCommand;
import com.homfo.user.dto.MarketingAgreementDto;
import com.homfo.user.dto.UserDto;
import com.homfo.user.dto.UserMarketingAgreementDto;
import com.homfo.user.infra.enums.UserStatus;
import com.homfo.user.port.LoadUserMarketingAgreementPort;
import com.homfo.user.port.LoadUserPort;
import com.homfo.user.port.ManageUserPort;
import com.homfo.user.port.ManageUserMarketingAgreementPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

class ManageUserServiceTest {

    @Mock
    private LoadUserPort loadUserPort;

    @Mock
    private LoadJwtPort loadJwtPort;

    @Mock
    private LoadUserMarketingAgreementPort loadUserMarketingAgreementPort;

    @Mock
    private ManageUserPort manageUserAccountPort;

    @Mock
    private ManageJwtPort manageJwtPort;

    @Mock
    private ManageUserMarketingAgreementPort manageUserMarketingAgreementPort;

    private JwtSecretDto accessTokenInfo = new JwtSecretDto("accessSecret", 10);

    private JwtSecretDto refreshTokenInfo = new JwtSecretDto("refreshSecret", 10);

    private ManageUserService manageUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        manageUserService = new ManageUserService(
                loadUserPort,
                loadJwtPort,
                loadUserMarketingAgreementPort,
                manageUserAccountPort,
                manageJwtPort,
                manageUserMarketingAgreementPort,
                accessTokenInfo,
                refreshTokenInfo
        );
    }

    @Test
    @DisplayName("사용자 등록 시 서비스 호출과 JWT 생성을 확인한다")
    void register_Success() {
        // given
        long userId = 1L;
        List<MarketingAgreementDto> marketingAgreementDtoList = new ArrayList<>(List.of(new MarketingAgreementDto(MarketingCode.SEND_INFORMATION_TO_THIRD_PARTY, true)));
        RegisterCommand command = new RegisterCommand("testUser", "password", "testUser", "999-9999-9999", Gender.MAN, "job", LocalDate.now(), marketingAgreementDtoList);
        UserDto userDto = new UserDto(userId, "testUser", "testUser", "999-9999-9999", Gender.MAN, "job", LocalDate.now(), UserStatus.USE);
        String expectedAccessToken = "accessToken";
        String expectedRefreshToken = "refreshToken";

        when(manageUserAccountPort.register(command)).thenReturn(userDto);
        when(manageJwtPort.save(userDto.id(), refreshTokenInfo)).thenReturn(expectedRefreshToken);

        try (MockedStatic<JwtUtil> jwtUtil = Mockito.mockStatic(JwtUtil.class)) {
            jwtUtil.when(() -> JwtUtil.createToken(eq(userDto.id()), eq(accessTokenInfo))).thenReturn(expectedAccessToken);

            // When
            JwtDto result = manageUserService.register(command);

            // Then
            assertThat(result.accessToken()).isEqualTo(expectedAccessToken);
            assertThat(result.refreshToken()).isEqualTo(expectedRefreshToken);

            verify(manageUserAccountPort, times(1)).register(command);
            verify(manageJwtPort, times(1)).save(userDto.id(), refreshTokenInfo);
            verify(manageUserMarketingAgreementPort, times(1)).save(command, userDto);
        }
    }

    @Test
    void deleteAccount_Success() {
        // given
        long userId = 1L;
        willDoNothing().given(manageUserAccountPort).deleteAccount(userId);
        willDoNothing().given(manageJwtPort).deleteToken(userId);

        // when
        manageUserService.deleteAccount(userId);

        // then
        then(manageUserAccountPort).should().deleteAccount(userId);
        then(manageJwtPort).should().deleteToken(userId);
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

        when(loadUserPort.signIn(command)).thenReturn(userDto);
        when(manageJwtPort.save(userDto.id(), refreshTokenInfo)).thenReturn(expectedRefreshToken);

        try (MockedStatic<JwtUtil> jwtUtil = Mockito.mockStatic(JwtUtil.class)) {
            jwtUtil.when(() -> JwtUtil.createToken(eq(userDto.id()), eq(accessTokenInfo))).thenReturn(expectedAccessToken);

            // When
            JwtDto result = manageUserService.signIn(command);

            // Then
            assertThat(result.accessToken()).isEqualTo(expectedAccessToken);
            assertThat(result.refreshToken()).isEqualTo(expectedRefreshToken);
            verify(loadUserPort, times(1)).signIn(command);
            verify(manageJwtPort, times(1)).save(userDto.id(), refreshTokenInfo);
        }
    }

    @Test
    @DisplayName("토큰 갱신 시 새로운 JWT 정보를 반환한다")
    void givenUserIdAndRefreshToken_whenExecute_thenReturnsNewJwtDto() {
        // Given
        long userId = 1L;
        TokenRefreshCommand command = new TokenRefreshCommand("refreshToken");
        String expectedRefreshToken = command.token();
        String expectedAccessToken = "newAccessToken";

        when(loadJwtPort.getVerifyToken(command.token(), refreshTokenInfo)).thenReturn(expectedRefreshToken);

        try (MockedStatic<JwtUtil> jwtUtil = Mockito.mockStatic(JwtUtil.class)) {
            jwtUtil.when(() -> JwtUtil.createToken(eq(userId), any(JwtSecretDto.class))).thenReturn(expectedAccessToken);

            // When
            JwtDto result = manageUserService.refreshToken(userId, command);

            // Then
            assertThat(result.accessToken()).isEqualTo(expectedAccessToken);
            assertThat(result.refreshToken()).isEqualTo(expectedRefreshToken);
            verify(loadJwtPort, times(1)).getVerifyToken(command.token(), refreshTokenInfo);
        }
    }

    @Test
    @DisplayName("사용자 정보와 마케팅 정보를 포함하여 반환한다")
    void givenUserId_whenExecute_thenReturnsUserMarketingAgreementDto() {
        // Given
        long userId = 1L;
        UserDto userDto = new UserDto(userId, "testUser", "testUser", "999-9999-9999", Gender.MAN, "job", LocalDate.now(), UserStatus.USE);
        List<MarketingAgreementDto> marketingAgreementDtoList = new ArrayList<>(List.of(new MarketingAgreementDto(MarketingCode.SEND_INFORMATION_TO_THIRD_PARTY, true)));
        UserMarketingAgreementDto expectedDto = new UserMarketingAgreementDto(userDto, marketingAgreementDtoList);

        when(loadUserPort.loadUser(userId)).thenReturn(userDto);
        when(loadUserMarketingAgreementPort.loadMarketingAgreement(userDto)).thenReturn(expectedDto);

        // When
        UserMarketingAgreementDto resultDto = manageUserService.getUserInfo(userId);

        // Then
        assertThat(resultDto).isEqualTo(expectedDto);
        verify(loadUserPort, times(1)).loadUser(userId);
        verify(loadUserMarketingAgreementPort, times(1)).loadMarketingAgreement(userDto);
    }
}
