package com.homfo.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 토큰 DTO 입니다.
 * */
public record JwtDto(
        /**
         * 액세스 토큰입니다.
         * */
        @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c", description = "액세스 토큰")
        String accessToken,

        /**
         * 리프레쉬 토큰입니다. 암호화하지 않은 원본이어야 합니다.
         * */
        @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c", description = "리프레쉬 토큰")
        String refreshToken
) {
}
