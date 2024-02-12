package com.hompo.user.service;

import com.hompo.enums.MarketingCode;
import com.hompo.user.dto.MarketingAgreementDto;
import com.hompo.user.dto.UserDto;
import com.hompo.user.dto.UserMarketingAgreementDto;
import com.hompo.user.entity.MySqlUserMarketingAgreement;
import com.hompo.user.repository.UserMarketingAgreementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class MySqlUserMarketingAgreementReadServiceTest {

    @Mock
    private UserMarketingAgreementRepository userMarketingAgreementRepository;

    @InjectMocks
    private MySqlUserMarketingAgreementReadService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("사용자 마케팅 동의 정보를 정확하게 가져온다")
    void getUserMarketingInfo_ShouldReturnCorrectData() {
        // Given
        long userId = 1L;
        UserDto userDto = new UserDto(userId, "username", "nickname", "010-1234-5678", null, "job", null, null);
        List<MySqlUserMarketingAgreement> agreements = List.of(
                new MySqlUserMarketingAgreement(userId, MarketingCode.sendInformationToThirdParty, true)
        );

        when(userMarketingAgreementRepository.findByUserId(anyLong())).thenReturn(agreements);

        // When
        UserMarketingAgreementDto result = service.getUserMarketingInfo(userDto);

        // Then
        assertThat(result.userDto()).isEqualTo(userDto);
        assertThat(result.marketingAgreementList()).hasSize(1)
                .containsExactlyInAnyOrder(
                        new MarketingAgreementDto(MarketingCode.sendInformationToThirdParty, true)
                );
    }
}
