package com.homfo.config;

import com.homfo.error.ErrorCode;
import com.homfo.error.ResourceAlreadyExistException;
import com.homfo.error.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException e) {
        log.warn("handleResourceNotFound", e);
        return ResponseEntity.status(400).body(makeErrorResponse(e.getErrorCode()));
    }

    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<Object> handleResourceAlreadyExist(ResourceAlreadyExistException e) {
        log.warn("handleResourceAlreadyExist", e);
        return ResponseEntity.status(400).body(makeErrorResponse(e.getErrorCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        log.warn("handleException", e);
        return ResponseEntity.status(500).body(new ErrorResponse("SERVER_ERROR_00000001", "서버에서 에러가 발생했습니다."));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
    }
}

record ErrorResponse(String code, String message) {
}
