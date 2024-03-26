package com.homfo.user.service;

import com.homfo.auth.command.TokenRefreshCommand;
import com.homfo.auth.dto.JwtDto;
import com.homfo.auth.dto.JwtSecretDto;
import com.homfo.auth.infra.util.JwtUtil;
import com.homfo.auth.port.LoadJwtPort;
import com.homfo.auth.port.ManageJwtPort;
import com.homfo.enums.Gender;
import com.homfo.enums.MarketingCode;
import com.homfo.error.BadRequestException;
import com.homfo.error.ResourceNotFoundException;
import com.homfo.sms.infra.enums.SmsErrorCode;
import com.homfo.sms.port.ManageSmsCodePort;
import com.homfo.user.command.RegisterCommand;
import com.homfo.user.command.SignInCommand;
import com.homfo.user.dto.MarketingAgreementDto;
import com.homfo.user.dto.UserDto;
import com.homfo.user.dto.UserMarketingAgreementDto;
import com.homfo.user.infra.enums.UserStatus;
import com.homfo.user.port.LoadUserMarketingAgreementPort;
import com.homfo.user.port.LoadUserPort;
import com.homfo.user.port.ManageUserMarketingAgreementPort;
import com.homfo.user.port.ManageUserPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Mock
    private ManageSmsCodePort manageSmsCodePort;

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
                manageSmsCodePort,
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

        when(manageSmsCodePort.existSuccessSmsCode(command.phoneNumber())).thenReturn(true);
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
    @DisplayName("성공한 인증 코드를 찾지 못 했다면 BadRequestException가 발생한다")
    void register_ThrowsBadRequestException_WhenNotExistSmsCode() {
        // given
        List<MarketingAgreementDto> marketingAgreementDtoList = new ArrayList<>(List.of(new MarketingAgreementDto(MarketingCode.SEND_INFORMATION_TO_THIRD_PARTY, true)));
        RegisterCommand command = new RegisterCommand("testUser", "password", "testUser", "999-9999-9999", Gender.MAN, "job", LocalDate.now(), marketingAgreementDtoList);

        when(manageSmsCodePort.existSuccessSmsCode(command.phoneNumber())).thenThrow(new ResourceNotFoundException(SmsErrorCode.NOT_EXIST_SMS));

        Executable result = () -> manageUserService.register(command);

        assertThrows(BadRequestException.class, result);
    }

    @Test
    @DisplayName("성공한 인증 코드가 이미 만료되었다면 BadRequestException가 발생한다")
    void register_ThrowsBadRequestException_WhenSmsCodeExpired() {
        // given
        List<MarketingAgreementDto> marketingAgreementDtoList = new ArrayList<>(List.of(new MarketingAgreementDto(MarketingCode.SEND_INFORMATION_TO_THIRD_PARTY, true)));
        RegisterCommand command = new RegisterCommand("testUser", "password", "testUser", "999-9999-9999", Gender.MAN, "job", LocalDate.now(), marketingAgreementDtoList);

        when(manageSmsCodePort.existSuccessSmsCode(command.phoneNumber())).thenReturn(false);

        Executable result = () -> manageUserService.register(command);

        assertThrows(BadRequestException.class, result);
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
