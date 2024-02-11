package com.hompo.auth.dto;

/**
 * 토큰 DTO 입니다.
 * */
public record JwtDto(
        /**
         * 액세스 토큰입니다.
         * */
        String accessToken,

        /**
         * 리프레쉬 토큰입니다. 암호화하지 않은 원본이어야 합니다.
         * */
        String refreshToken
) {
}
