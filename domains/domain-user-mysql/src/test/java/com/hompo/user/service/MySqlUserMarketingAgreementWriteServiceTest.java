package com.hompo.user.service;

import com.hompo.enums.MarketingCode;
import com.hompo.user.command.RegisterCommand;
import com.hompo.user.dto.MarketingAgreementDto;
import com.hompo.user.dto.UserDto;
import com.hompo.user.entity.MySqlUserMarketingAgreement;
import com.hompo.user.repository.UserMarketingAgreementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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
                List.of(new MarketingAgreementDto(MarketingCode.sendInformationToThirdParty, true))
        );
        UserDto userDto = new UserDto(userId, "testAccount", "testNickname", "010-1234-5678", null, "Developer", LocalDate.now(), null);

        // When
        service.save(command, userDto);

        // Then
        verify(userMarketingAgreementRepository).saveAll(anyList());
    }
}
