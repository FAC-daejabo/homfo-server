package com.homfo.employee.command;

import com.homfo.employee.command.RegisterCommand;
import com.homfo.employee.dto.MarketingAgreementDto;
import com.homfo.enums.Gender;
import com.homfo.enums.MarketingCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RegisterCommandTest {
    @Test
    @DisplayName("회원가입 커맨드가 올바르게 생성되어야 한다.")
    void registerCommandIsCreatedProperly() {
        // given
        String account = "testAccount";
        String password = "testPW@111";
        String nickname = "닉네임";
        String phoneNumber = "010-1234-1234";
        Gender gender = Gender.MAN; // Gender enum이 M,F 등으로 정의되었다고 가정
        String job = "학생";
        LocalDate birthday = LocalDate.of(2000, 12, 12);
        List<MarketingAgreementDto> marketingCodeList = List.of(new MarketingAgreementDto(MarketingCode.SEND_INFORMATION_TO_THIRD_PARTY, true));

        // when
        RegisterCommand command = new RegisterCommand(account, password, nickname, phoneNumber, gender, job, birthday, marketingCodeList);

        // then
        assertThat(command.account()).isEqualTo(account);
        assertThat(command.password()).isEqualTo(password);
        assertThat(command.nickname()).isEqualTo(nickname);
        assertThat(command.phoneNumber()).isEqualTo(phoneNumber);
        assertThat(command.gender()).isEqualTo(gender);
        assertThat(command.job()).isEqualTo(job);
        assertThat(command.birthday()).isEqualTo(birthday);
        assertThat(command.marketingCodeList()).hasSize(1);
        assertThat(command.marketingCodeList().get(0).code()).isEqualTo(MarketingCode.SEND_INFORMATION_TO_THIRD_PARTY);
        assertThat(command.marketingCodeList().get(0).isAgreement()).isTrue();
    }
}
