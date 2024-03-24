package com.homfo.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RequestLimitException extends RuntimeException {
    private final ErrorCode errorCode;
}
