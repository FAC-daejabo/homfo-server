package com.homfo.user.dto;

public record SmsSendDto(
        String phoneNumber,

        String message
) {
}
