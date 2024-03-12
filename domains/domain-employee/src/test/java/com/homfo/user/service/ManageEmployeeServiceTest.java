package com.homfo.user.service;

import com.homfo.auth.command.TokenRefreshCommand;
import com.homfo.auth.dto.JwtDto;
import com.homfo.auth.dto.JwtSecretDto;
import com.homfo.auth.infra.util.JwtUtil;
import com.homfo.auth.port.LoadJwtPort;
import com.homfo.auth.port.ManageJwtPort;
import com.homfo.user.command.RegisterCommand;
import com.homfo.user.command.SignInCommand;
import com.homfo.user.dto.EmployeeDto;
import com.homfo.user.dto.EmployeeMarketingAgreementDto;
import com.homfo.user.dto.MarketingAgreementDto;
import com.homfo.user.infra.enums.EmployeeRole;
import com.homfo.user.infra.enums.EmployeeStatus;
import com.homfo.user.port.LoadEmployeeMarketingAgreementPort;
import com.homfo.user.port.LoadEmployeePort;
import com.homfo.user.port.ManageEmployeeAccountPort;
import com.homfo.user.port.ManageEmployeeMarketingAgreementPort;
import com.homfo.enums.Gender;
import com.homfo.enums.MarketingCode;
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

class ManageEmployeeServiceTest {

    @Mock
    private LoadEmployeePort loadEmployeePort;

    @Mock
    private LoadJwtPort loadJwtPort;

    @Mock
    private LoadEmployeeMarketingAgreementPort loadEmployeeMarketingAgreementPort;

    @Mock
    private ManageEmployeeAccountPort manageEmployeeAccountPort;

    @Mock
    private ManageJwtPort manageJwtPort;

    @Mock
    private ManageEmployeeMarketingAgreementPort manageEmployeeMarketingAgreementPort;

    private JwtSecretDto employeeAccessTokenInfo = new JwtSecretDto("accessSecret", 10);

    private JwtSecretDto employeeRefreshTokenInfo = new JwtSecretDto("refreshSecret", 10);

    private ManageEmployeeService manageEmployeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        manageEmployeeService = new ManageEmployeeService(
                loadEmployeePort,
                loadJwtPort,
                loadEmployeeMarketingAgreementPort,
                manageEmployeeAccountPort,
                manageJwtPort,
                manageEmployeeMarketingAgreementPort,
                employeeAccessTokenInfo,
                employeeRefreshTokenInfo
        );
    }

    @Test
    @DisplayName("사용자 등록 시 서비스 호출과 JWT 생성을 확인한다")
    void register_Success() {
        // given
        long employeeId = 1L;
        List<MarketingAgreementDto> marketingAgreementDtoList = new ArrayList<>(List.of(new MarketingAgreementDto(MarketingCode.SEND_INFORMATION_TO_THIRD_PARTY, true)));
        RegisterCommand command = new RegisterCommand("testEmployee", "password", "testEmployee", "999-9999-9999", Gender.MAN, "job", LocalDate.now(), marketingAgreementDtoList);
        EmployeeDto employeeDto = new EmployeeDto(employeeId, "testEmployee", "testEmployee", "999-9999-9999", Gender.MAN, "job", LocalDate.now(), EmployeeStatus.PENDING, EmployeeRole.EMPLOYEE);
        String expectedAccessToken = "accessToken";
        String expectedRefreshToken = "refreshToken";

        when(manageEmployeeAccountPort.register(command)).thenReturn(employeeDto);
        when(manageJwtPort.save(employeeDto.id(), employeeRefreshTokenInfo)).thenReturn(expectedRefreshToken);

        try (MockedStatic<JwtUtil> jwtUtil = Mockito.mockStatic(JwtUtil.class)) {
            jwtUtil.when(() -> JwtUtil.createToken(eq(employeeDto.id()), eq(employeeAccessTokenInfo))).thenReturn(expectedAccessToken);

            // When
            JwtDto result = manageEmployeeService.register(command);

            // Then
            assertThat(result.accessToken()).isEqualTo(expectedAccessToken);
            assertThat(result.refreshToken()).isEqualTo(expectedRefreshToken);

            verify(manageEmployeeAccountPort, times(1)).register(command);
            verify(manageJwtPort, times(1)).save(employeeDto.id(), employeeRefreshTokenInfo);
            verify(manageEmployeeMarketingAgreementPort, times(1)).save(command, employeeDto);
        }
    }

    @Test
    void deleteAccount_Success() {
        // given
        long employeeId = 1L;
        willDoNothing().given(manageEmployeeAccountPort).deleteAccount(employeeId);
        willDoNothing().given(manageJwtPort).deleteToken(employeeId);

        // when
        manageEmployeeService.deleteAccount(employeeId);

        // then
        then(manageEmployeeAccountPort).should().deleteAccount(employeeId);
        then(manageJwtPort).should().deleteToken(employeeId);
    }

    @Test
    @DisplayName("로그인 시 JWT 정보를 반환한다")
    void givenSignInCommand_whenExecute_thenReturnsJwtDto() {
        // Given
        long employeeId = 1L;
        SignInCommand command = new SignInCommand("testEmployee", "password");
        EmployeeDto employeeDto = new EmployeeDto(employeeId, "testEmployee", "testEmployee", "999-9999-9999", Gender.MAN, "job", LocalDate.now(), EmployeeStatus.PENDING, EmployeeRole.EMPLOYEE);
        String expectedAccessToken = "accessToken";
        String expectedRefreshToken = "refreshToken";

        when(loadEmployeePort.signIn(command)).thenReturn(employeeDto);
        when(manageJwtPort.save(employeeDto.id(), employeeRefreshTokenInfo)).thenReturn(expectedRefreshToken);

        try (MockedStatic<JwtUtil> jwtUtil = Mockito.mockStatic(JwtUtil.class)) {
            jwtUtil.when(() -> JwtUtil.createToken(eq(employeeDto.id()), eq(employeeAccessTokenInfo))).thenReturn(expectedAccessToken);

            // When
            JwtDto result = manageEmployeeService.signIn(command);

            // Then
            assertThat(result.accessToken()).isEqualTo(expectedAccessToken);
            assertThat(result.refreshToken()).isEqualTo(expectedRefreshToken);
            verify(loadEmployeePort, times(1)).signIn(command);
            verify(manageJwtPort, times(1)).save(employeeDto.id(), employeeRefreshTokenInfo);
        }
    }

    @Test
    @DisplayName("토큰 갱신 시 새로운 JWT 정보를 반환한다")
    void givenEmployeeIdAndRefreshToken_whenExecute_thenReturnsNewJwtDto() {
        // Given
        long employeeId = 1L;
        TokenRefreshCommand command = new TokenRefreshCommand("refreshToken");
        String expectedRefreshToken = command.token();
        String expectedAccessToken = "newAccessToken";

        when(loadJwtPort.getVerifyToken(command.token(), employeeRefreshTokenInfo)).thenReturn(expectedRefreshToken);

        try (MockedStatic<JwtUtil> jwtUtil = Mockito.mockStatic(JwtUtil.class)) {
            jwtUtil.when(() -> JwtUtil.createToken(eq(employeeId), any(JwtSecretDto.class))).thenReturn(expectedAccessToken);

            // When
            JwtDto result = manageEmployeeService.refreshToken(employeeId, command);

            // Then
            assertThat(result.accessToken()).isEqualTo(expectedAccessToken);
            assertThat(result.refreshToken()).isEqualTo(expectedRefreshToken);
            verify(loadJwtPort, times(1)).getVerifyToken(command.token(), employeeRefreshTokenInfo);
        }
    }

    @Test
    @DisplayName("사용자 정보와 마케팅 정보를 포함하여 반환한다")
    void givenEmployeeId_whenExecute_thenReturnsEmployeeMarketingAgreementDto() {
        // Given
        long employeeId = 1L;
        EmployeeDto employeeDto = new EmployeeDto(employeeId, "testEmployee", "testEmployee", "999-9999-9999", Gender.MAN, "job", LocalDate.now(), EmployeeStatus.PENDING, EmployeeRole.EMPLOYEE);
        List<MarketingAgreementDto> marketingAgreementDtoList = new ArrayList<>(List.of(new MarketingAgreementDto(MarketingCode.SEND_INFORMATION_TO_THIRD_PARTY, true)));
        EmployeeMarketingAgreementDto expectedDto = new EmployeeMarketingAgreementDto(employeeDto, marketingAgreementDtoList);

        when(loadEmployeePort.loadEmployee(employeeId)).thenReturn(employeeDto);
        when(loadEmployeeMarketingAgreementPort.loadMarketingAgreement(employeeDto)).thenReturn(expectedDto);

        // When
        EmployeeMarketingAgreementDto resultDto = manageEmployeeService.getEmployeeInfo(employeeId);

        // Then
        assertThat(resultDto).isEqualTo(expectedDto);
        verify(loadEmployeePort, times(1)).loadEmployee(employeeId);
        verify(loadEmployeeMarketingAgreementPort, times(1)).loadMarketingAgreement(employeeDto);
    }
}
