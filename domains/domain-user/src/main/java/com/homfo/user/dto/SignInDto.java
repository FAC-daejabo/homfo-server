package com.homfo.user.dto;

/**
 * 로그인 성공 시 응답 DTO 입니다.
 * */
public record SignInDto(
        String accessToken,

          String refreshToken
) {
}
