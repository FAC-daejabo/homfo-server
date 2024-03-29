package com.homfo.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ThirdPartyUnavailableException extends RuntimeException {
    private final ErrorCode errorCode;
}
