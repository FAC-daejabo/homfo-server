package com.homfo.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResourceAlreadyExistException extends ClientException {
    private final ErrorCode errorCode;
}
