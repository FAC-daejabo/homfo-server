package com.homfo.user.command;

/**
 * 전화번호 인증 코드를 확인하기 위한 Command 입니다.
 * */
public record ValidateSmsCodeCommand(
        String phoneNumber,

        String code
) {
}
