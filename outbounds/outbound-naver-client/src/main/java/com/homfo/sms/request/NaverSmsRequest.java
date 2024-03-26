package com.homfo.sms.request;

import java.util.List;

public record NaverSmsRequest(
        String type,

        String contentType,

        String countryCode,

        String from,

        String content,

        List<NaverSmsPayload> messages
) {

}
