package com.homfo.user.request;

import com.homfo.user.command.SignInCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

/**
 * 로그인을 위한 Command 입니다.
 * */
public record SignInRequest(
        @Schema(example = "testAccount", description = "계정")
        @Pattern(regexp = "^[a-zA-Z\\d]{8,15}$", message = "올바르지 않은 계정 또는 비밀번호입니다.")
        String account,

        @Schema(example = "testPW@111", description = "비밀번호")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,15}$", message = "올바르지 않은 계정 또는 비밀번호입니다.")
        String password
) {
        public SignInCommand toCommand() {
                return new SignInCommand(account, password);
        }
}
