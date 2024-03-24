package com.homfo.user.dto;

import java.time.LocalDateTime;

public record SmsCodeDto(
        String phoneNumber,

        String code,

        Integer count,

        LocalDateTime updatedAt
) {
}
