package com.hompo.auth.dto;

/**
 * 토큰 정보 DTO 입니다.
 *
 * Swagger에는 노출하지 않습니다.
 * */
public record JwtSecretDto(
        /**
         * 토큰 생성에 사용되는 signature 입니다.
         * */
        String secretKey,

        /**
         * 토큰 만료 시간입니다.
         * */
        long expireTime
) {

}
