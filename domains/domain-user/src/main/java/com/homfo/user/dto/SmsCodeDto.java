package com.homfo.user.dto;

public record SmsCodeDto(
        String phoneNumber,

        String code
) {
}
