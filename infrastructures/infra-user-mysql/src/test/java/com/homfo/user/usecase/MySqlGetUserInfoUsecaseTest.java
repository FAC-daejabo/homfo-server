package com.homfo.user.usecase;

import com.homfo.enums.Gender;
import com.homfo.enums.MarketingCode;
import com.homfo.user.dto.MarketingAgreementDto;
import com.homfo.user.dto.UserDto;
import com.homfo.user.dto.UserMarketingAgreementDto;
import com.homfo.user.infra.enums.UserStatus;
import com.homfo.user.service.UserMarketingAgreementReadService;
import com.homfo.user.service.UserReadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MySqlGetUserInfoUsecaseTest {

    @Mock
    private UserReadService userReadService;

    @Mock
    private UserMarketingAgreementReadService userMarketingAgreementReadService;

    @InjectMocks
    private MySqlGetUserInfoUsecase getUserInfoUsecase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("사용자 정보와 마케팅 정보를 포함하여 반환한다")
    void givenUserId_whenExecute_thenReturnsUserMarketingAgreementDto() {
        // Given
        long userId = 1L;
        UserDto userDto = new UserDto(userId, "testUser", "testUser", "999-9999-9999", Gender.MAN, "job", LocalDate.now(), UserStatus.USE);
        List<MarketingAgreementDto> marketingAgreementDtoList = new ArrayList<>(List.of(new MarketingAgreementDto(MarketingCode.SendInformationToThirdParty, true)));
        UserMarketingAgreementDto expectedDto = new UserMarketingAgreementDto(userDto, marketingAgreementDtoList);

        when(userReadService.findById(userId)).thenReturn(userDto);
        when(userMarketingAgreementReadService.getUserMarketingInfo(userDto)).thenReturn(expectedDto);

        // When
        UserMarketingAgreementDto resultDto = getUserInfoUsecase.execute(userId);

        // Then
        assertThat(resultDto).isEqualTo(expectedDto);
        verify(userReadService, times(1)).findById(userId);
        verify(userMarketingAgreementReadService, times(1)).getUserMarketingInfo(userDto);
    }
}
