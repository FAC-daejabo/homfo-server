package com.homfo.user.request;

import com.homfo.enums.Gender;
import com.homfo.user.command.RegisterCommand;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

/**
 * 회원가입을 위한 Command 입니다.
 * */
public record RegisterRequest(
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

        List<MarketingAgreementRequest> marketingCodeList
) {
        public RegisterCommand toCommand() {
                return new RegisterCommand(
                        account,
                        password,
                        nickname,
                        phoneNumber,
                        gender,
                        job,
                        birthday,
                        marketingCodeList.stream().map(MarketingAgreementRequest::toDto).toList()
                );
        }
}
