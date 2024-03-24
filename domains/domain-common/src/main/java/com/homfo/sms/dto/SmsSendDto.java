package com.homfo.sms.dto;

/**
 * 문자 전송을 위한 DTO입니다.
 * */
public record SmsSendDto(
        String phoneNumber,

        String message
) {
}
