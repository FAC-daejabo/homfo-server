package com.homfo.user.adapter;

import com.homfo.enums.MarketingCode;
import com.homfo.user.adapter.UserMarketingAgreementPersistenceAdapter;
import com.homfo.user.command.RegisterCommand;
import com.homfo.user.dto.MarketingAgreementDto;
import com.homfo.user.dto.UserDto;
import com.homfo.user.dto.UserMarketingAgreementDto;
import com.homfo.user.entity.JpaUserMarketingAgreement;
import com.homfo.user.repository.UserMarketingAgreementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserMarketingAgreementPersistenceAdapterTest {

    @Mock
    private UserMarketingAgreementRepository userMarketingAgreementRepository;

    @InjectMocks
    private UserMarketingAgreementPersistenceAdapter adpater;

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
        List<JpaUserMarketingAgreement> agreements = List.of(
                new JpaUserMarketingAgreement(userId, MarketingCode.SEND_INFORMATION_TO_THIRD_PARTY, true)
        );

        when(userMarketingAgreementRepository.findByUserId(anyLong())).thenReturn(agreements);

        // When
        UserMarketingAgreementDto result = adpater.loadMarketingAgreement(userDto);

        // Then
        assertThat(result.userDto()).isEqualTo(userDto);
        assertThat(result.marketingAgreementList()).hasSize(1)
                .containsExactlyInAnyOrder(
                        new MarketingAgreementDto(MarketingCode.SEND_INFORMATION_TO_THIRD_PARTY, true)
                );
    }

    @Test
    @DisplayName("사용자 마케팅 동의 정보 저장")
    void saveMarketingAgreement() {
        // Given
        long userId = 1L;
        RegisterCommand command = new RegisterCommand(
                "testAccount",
                "testPassword",
                "testNickname",
                "010-1234-5678",
                null,
                "Developer",
                LocalDate.now(),
                List.of(new MarketingAgreementDto(MarketingCode.SEND_INFORMATION_TO_THIRD_PARTY, true))
        );
        UserDto userDto = new UserDto(userId, "testAccount", "testNickname", "010-1234-5678", null, "Developer", LocalDate.now(), null);

        // When
        adpater.save(command, userDto);

        // Then
        verify(userMarketingAgreementRepository).saveAll(anyList());
    }
}
