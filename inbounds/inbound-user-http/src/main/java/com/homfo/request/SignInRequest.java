package com.homfo.request;

import com.homfo.user.command.SignInCommand;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 로그인을 위한 Command 입니다.
 * */
public record SignInRequest(
        @Schema(example = "testAccount", description = "계정")
        String account,

        @Schema(example = "testPW@111", description = "비밀번호")
        String password
) {
        public SignInCommand toCommand() {
                return new SignInCommand(account, password);
        }
}
