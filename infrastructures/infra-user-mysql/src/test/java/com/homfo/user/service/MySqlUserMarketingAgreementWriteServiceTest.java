package com.homfo.user.service;

import com.homfo.enums.MarketingCode;
import com.homfo.user.command.RegisterCommand;
import com.homfo.user.dto.MarketingAgreementDto;
import com.homfo.user.dto.UserDto;
import com.homfo.user.repository.UserMarketingAgreementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;

class MySqlUserMarketingAgreementWriteServiceTest {

    @Mock
    private UserMarketingAgreementRepository userMarketingAgreementRepository;

    @InjectMocks
    private MySqlUserMarketingAgreementWriteService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
                List.of(new MarketingAgreementDto(MarketingCode.SendInformationToThirdParty, true))
        );
        UserDto userDto = new UserDto(userId, "testAccount", "testNickname", "010-1234-5678", null, "Developer", LocalDate.now(), null);

        // When
        service.save(command, userDto);

        // Then
        verify(userMarketingAgreementRepository).saveAll(anyList());
    }
}
