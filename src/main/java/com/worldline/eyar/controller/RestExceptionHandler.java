package com.worldline.eyar.controller;


import com.worldline.eyar.common.GeneralResponse;
import com.worldline.eyar.common.response.ErrorResponse;
import com.worldline.eyar.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> exceptionHandler(Exception exception) {
        log.error(exception.getLocalizedMessage(), exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                GeneralResponse.builder().body(makeExceptionResponse(exception)).status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<?> exceptionHandler(BindException exception) {
        log.error(exception.getLocalizedMessage(), exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                GeneralResponse.builder().body(makeExceptionResponse(exception)).status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<?> exceptionHandler(AccessDeniedException exception) {
        log.error(exception.getLocalizedMessage(), exception);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                GeneralResponse.builder().body(makeExceptionResponse(exception)).status(HttpStatus.FORBIDDEN).build());
    }

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<?> businessExceptionHandler(BusinessException businessException) {
        log.error(businessException.getLocalizedMessage(), businessException);
        return ResponseEntity.status(businessException.getHttpStatus()).body(
                GeneralResponse.builder().body(makeExceptionResponse(businessException)).status(businessException.getHttpStatus()).build());
    }

    private ErrorResponse makeExceptionResponse(Throwable throwable){
        return ErrorResponse.builder()
                .errorMessage(throwable.getLocalizedMessage())
                .build();
    }

    private ErrorResponse makeExceptionResponse(BindException exception){
        StringBuilder builder = new StringBuilder();
        exception.getAllErrors().forEach(error -> builder.append(error.getDefaultMessage()).append(" "));
        return ErrorResponse.builder()
                .errorMessage(builder.toString())
                .build();
    }

}
