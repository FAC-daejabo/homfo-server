package com.hompo.user.command;

import com.hompo.enums.Gender;
import com.hompo.enums.MarketingCode;
import com.hompo.user.dto.MarketingAgreementDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record RegisterCommand(

        @Schema(example = "testAccount", description = "계정")
        String account,

        @Schema(example = "testPW@111", description = "비밀번호")
        String password,

        @Schema(example = "닉네임", description = "닉네임")
        String nickname,

        @Schema(example = "010-1234-1234", description = "전화번호")
        String phoneNumber,

        @Schema(example = "M", description = "성별")
        Gender gender,

        @Schema(example = "학생", description = "직업")
        String job,

        @Schema(example = "2000-12-12", description = "생년월일")
        LocalDate birthday,

        List<MarketingAgreementDto> marketingCodeList
) {
}
