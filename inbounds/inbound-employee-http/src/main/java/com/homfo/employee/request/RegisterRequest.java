package com.homfo.employee.request;

import com.homfo.employee.command.RegisterCommand;
import com.homfo.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.List;

/**
 * 회원가입을 위한 Command 입니다.
 */
public record RegisterRequest(
        @Schema(example = "testAccount", description = "계정")
        @Pattern(regexp = "^[a-zA-Z\\d]{8,15}$", message = "올바르지 않은 계정 또는 비밀번호입니다.")
        String account,

        @Schema(example = "testPW@111", description = "비밀번호")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,15}$", message = "올바르지 않은 계정 또는 비밀번호입니다.")
        String password,

        @Schema(example = "이름", description = "홍길동")
        @Pattern(regexp = "^[가-힣]{2,5}|[A-Za-z]{2,}\\s[A-Za-z]{2,}$", message = "올바르지 않은 이름입니다.")
        String name,

        @Schema(example = "010-1234-1234", description = "전화번호")
        @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "-가 포함된 모바일 전화번호 11자리여야 합니다.")
        String phoneNumber,

        @Schema(example = "M", description = "성별")
        @NotNull
        Gender gender,

        @Schema(example = "학생", description = "부서")
        @NotEmpty
        String department,

        @Schema(example = "2000-12-12", description = "생년월일")
        @NotNull
        LocalDate birthday,

        List<MarketingAgreementRequest> marketingCodeList
) {
    public RegisterCommand toCommand() {
        return new RegisterCommand(
                account,
                password,
                name,
                phoneNumber,
                gender,
                department,
                birthday,
                marketingCodeList.stream().map(MarketingAgreementRequest::toDto).toList()
        );
    }
}
