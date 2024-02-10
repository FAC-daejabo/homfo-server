package com.hompo.auth.dto;

public record JwtDto(
        String accessToken,

        String refreshToken
) {
}
