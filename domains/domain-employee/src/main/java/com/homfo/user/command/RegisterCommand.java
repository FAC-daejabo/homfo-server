package com.homfo.user.command;

import com.homfo.user.dto.MarketingAgreementDto;
import com.homfo.enums.Gender;

import java.time.LocalDate;
import java.util.List;

/**
 * 회원가입을 위한 Command 입니다.
 * */
public record RegisterCommand(
        String account,

        String password,

        String nickname,

        String phoneNumber,

        Gender gender,

        String job,

        LocalDate birthday,

        List<MarketingAgreementDto> marketingCodeList
) {
}
