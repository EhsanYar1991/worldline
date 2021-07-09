package com.worldline.eyar.controller;


import com.worldline.eyar.common.GeneralResponse;
import com.worldline.eyar.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> exceptionHandler(Exception exception) {
        log.error(exception.getLocalizedMessage(), exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                GeneralResponse.builder().body(exception).status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<?> businessExceptionHandler(BusinessException businessException) {
        log.error(businessException.getLocalizedMessage(), businessException);
        return ResponseEntity.status(businessException.getHttpStatus()).body(
                GeneralResponse.builder().body(businessException).status(businessException.getHttpStatus()).build());
    }

}
