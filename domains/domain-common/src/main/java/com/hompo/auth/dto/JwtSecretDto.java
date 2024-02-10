package com.hompo.auth.dto;

public record JwtSecretDto(
        String secretKey,

        long expireTime
) {

}
