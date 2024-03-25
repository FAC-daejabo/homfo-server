package com.homfo.user.request;

import com.homfo.sms.command.ValidateSmsCodeCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

public record ValidateSmsCodeRequest(
        @Schema(example = "010-0000-0000", description = "전화번호")
        @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "-가 포함된 모바일 전화번호 11자리여야 합니다.")
        String phoneNumber,

        @Schema(example = "951731", description = "인증번호")
        @Pattern(regexp = "^\\d{6}$", message = "6자리 숫자 문자열이어야 합니다.")
        String code
) {
    public ValidateSmsCodeCommand toCommand() {
        return new ValidateSmsCodeCommand(phoneNumber, code);
    }
}
