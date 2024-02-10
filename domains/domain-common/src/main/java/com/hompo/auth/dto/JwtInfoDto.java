package com.hompo.auth.dto;

public record JwtInfoDto(
        String secretKey,

        long expireTime
) {

}
