package com.homfo.sms.request;

public record NaverSmsPayload(
        String to,

        String subject,

        String message
) {

}
