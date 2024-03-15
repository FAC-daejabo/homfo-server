package com.homfo.user.adapter;

import com.homfo.user.command.RegisterCommand;
import com.homfo.user.dto.EmployeeDto;
import com.homfo.user.dto.EmployeeMarketingAgreementDto;
import com.homfo.user.dto.MarketingAgreementDto;
import com.homfo.user.entity.JpaEmployeeMarketingAgreement;
import com.homfo.user.repository.EmployeeMarketingAgreementRepository;
import com.homfo.enums.MarketingCode;
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

class EmployeeMarketingAgreementPersistenceAdapterTest {

    @Mock
    private EmployeeMarketingAgreementRepository employeeMarketingAgreementRepository;

    @InjectMocks
    private EmployeeMarketingAgreementPersistenceAdapter adpater;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("사용자 마케팅 동의 정보를 정확하게 가져온다")
    void getUserMarketingInfo_ShouldReturnCorrectData() {
        // Given
        long userId = 1L;
        EmployeeDto userDto = new EmployeeDto(userId, "username", "nickname", "010-1234-5678", null, "job", null, null, null);
        List<JpaEmployeeMarketingAgreement> agreements = List.of(
                new JpaEmployeeMarketingAgreement(userId, MarketingCode.SEND_INFORMATION_TO_THIRD_PARTY, true)
        );

        when(employeeMarketingAgreementRepository.findByEmployeeId(anyLong())).thenReturn(agreements);

        // When
        EmployeeMarketingAgreementDto result = adpater.loadMarketingAgreement(userDto);

        // Then
        assertThat(result.employee()).isEqualTo(userDto);
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
        EmployeeDto userDto = new EmployeeDto(userId, "testAccount", "testNickname", "010-1234-5678", null, "Developer", LocalDate.now(), null, null);

        // When
        adpater.save(command, userDto);

        // Then
        verify(employeeMarketingAgreementRepository).saveAll(anyList());
    }
}
