package com.homfo.auth.command;

/**
 * 사용자 액세스 토큰 리프레쉬 요청을 request body로 받기 위한 command 입니다.
 * */
public record TokenRefreshCommand(
        String token
) {
}
