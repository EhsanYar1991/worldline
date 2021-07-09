package com.worldline.eyar.controller;

import com.worldline.eyar.common.GeneralResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

public class BaseController {


    protected <T extends Serializable> ResponseEntity<GeneralResponse<?>> okResponse(T body) {
        return ResponseEntity.ok(GeneralResponse.builder().status(HttpStatus.OK).body(body).build());
    }

    protected <T extends Serializable> ResponseEntity<GeneralResponse<?>> response(HttpStatus httpStatus, T body) {
        return ResponseEntity.ok(GeneralResponse.builder().status(httpStatus).body(body).build());
    }

}
