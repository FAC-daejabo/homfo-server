package com.homfo.sms.dto;

public record SmsSendDto(
        String phoneNumber,

        String message
) {
}
