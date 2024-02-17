package com.homfo.user.command;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 로그인을 위한 Command 입니다.
 * */
public record SignInCommand(
        @Schema(example = "testAccount", description = "계정")
        String account,

        @Schema(example = "testPW@111", description = "비밀번호")
        String password
) {
}
