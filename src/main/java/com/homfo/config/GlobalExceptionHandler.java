package com.homfo.config;

import com.homfo.error.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<Object> handleOptimisticLocking(ObjectOptimisticLockingFailureException e) {
        log.warn("ObjectOptimisticLockingFailureException", e);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(makeErrorResponse(CommonErrorCode.DUPLICATE));
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<Object> handleClientException(ClientException e) {
        log.warn("ClientException", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(makeErrorResponse(CommonErrorCode.BAD_REQUEST));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        log.warn("handleException", e);
        return ResponseEntity.status(500).body(makeErrorResponse(CommonErrorCode.INTERNAL_SERVER_ERROR));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
    }
}

record ErrorResponse(String code, String message) {
}
