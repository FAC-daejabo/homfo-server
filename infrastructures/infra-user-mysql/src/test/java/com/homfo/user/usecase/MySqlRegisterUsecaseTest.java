package com.homfo.user.usecase;

import com.homfo.auth.dto.JwtDto;
import com.homfo.auth.dto.JwtSecretDto;
import com.homfo.auth.infra.util.JwtUtil;
import com.homfo.enums.Gender;
import com.homfo.enums.MarketingCode;
import com.homfo.user.command.RegisterCommand;
import com.homfo.user.dto.MarketingAgreementDto;
import com.homfo.user.dto.UserDto;
import com.homfo.user.infra.enums.UserStatus;
import com.homfo.user.service.UserMarketingAgreementWriteService;
import com.homfo.user.service.UserRefreshTokenWriteService;
import com.homfo.user.service.UserWriteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
class MySqlRegisterUsecaseTest {

    @Mock
    private UserWriteService userWriteService;

    @Mock
    private UserRefreshTokenWriteService userRefreshTokenWriteService;

    @Mock
    private UserMarketingAgreementWriteService userMarketingAgreementWriteService;

    @Mock(name = "userAccessTokenInfo")
    private JwtSecretDto userAccessTokenInfo;

    @Mock(name = "userRefreshTokenInfo")
    private JwtSecretDto userRefreshTokenInfo;

    // @InjectMocks
    // 아무리해도 userAccessTokenInfo랑 userRefreshTokenInfo가 분리돼서 주입되지 않고 둘 중 하나만 자꾸 주입된다...
    private MySqlRegisterUsecase registerUsecase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        registerUsecase = new MySqlRegisterUsecase(userWriteService, userRefreshTokenWriteService, userMarketingAgreementWriteService, userAccessTokenInfo, userRefreshTokenInfo);
    }

    @Test
    @DisplayName("사용자 등록 시 서비스 호출과 JWT 생성을 확인한다")
    void givenRegisterCommand_whenExecute_thenCallsServicesAndReturnsJwtDto() {
        // Given
        long userId = 1L;
        List<MarketingAgreementDto> marketingAgreementDtoList = new ArrayList<>(List.of(new MarketingAgreementDto(MarketingCode.SendInformationToThirdParty, true)));
        RegisterCommand command = new RegisterCommand("testUser", "password", "testUser", "999-9999-9999", Gender.MAN, "job", LocalDate.now(), marketingAgreementDtoList);
        UserDto userDto = new UserDto(userId, "testUser", "testUser", "999-9999-9999", Gender.MAN, "job", LocalDate.now(), UserStatus.USE);
        String expectedAccessToken = "accessToken";
        String expectedRefreshToken = "refreshToken";

        when(userWriteService.register(command)).thenReturn(userDto);
        when(userRefreshTokenWriteService.save(userDto.id(), userRefreshTokenInfo)).thenReturn(expectedRefreshToken);

        try (MockedStatic<JwtUtil> jwtUtil = Mockito.mockStatic(JwtUtil.class)) {
            jwtUtil.when(() -> JwtUtil.createToken(eq(userDto.id()), eq(userAccessTokenInfo))).thenReturn(expectedAccessToken);

            // When
            JwtDto result = registerUsecase.execute(command);

            // Then
            assertThat(result.accessToken()).isEqualTo(expectedAccessToken);
            assertThat(result.refreshToken()).isEqualTo(expectedRefreshToken);
            verify(userWriteService, times(1)).register(command);
            verify(userRefreshTokenWriteService, times(1)).save(userDto.id(), userRefreshTokenInfo);
            verify(userMarketingAgreementWriteService, times(1)).save(command, userDto);
        }
    }
}
