package com.worldline.eyar.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {

    private HttpStatus httpStatus;

    public BusinessException(String message) {
        super(message);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public BusinessException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public BusinessException(Throwable cause) {
        super(cause.getLocalizedMessage(), cause);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public BusinessException(HttpStatus httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public BusinessException(Throwable cause, HttpStatus httpStatus) {
        super(cause);
        this.httpStatus = httpStatus;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
