package com.hompo.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record SignInCommand(
        @Schema(example = "testAccount", description = "계정")
        String account,

        @Schema(example = "testPW@111", description = "비밀번호")
        String password
) {
}
