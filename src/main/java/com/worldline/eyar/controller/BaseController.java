package com.worldline.eyar.controller;

import com.worldline.eyar.common.GeneralResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.Serializable;

@CrossOrigin
public class BaseController {

    protected static final String ALL_AUTHORITY = "hasAnyAuthority('ADMIN','USER')";
    protected static final String ADMIN_AUTHORITY = "hasAuthority('ADMIN')";


    protected <T extends Serializable> ResponseEntity<GeneralResponse<?>> okResponse(T body) {
        return ResponseEntity.ok(GeneralResponse.builder().status(HttpStatus.OK).body(body).build());
    }

    protected <T extends Serializable> ResponseEntity<GeneralResponse<?>> response(HttpStatus httpStatus, T body) {
        return ResponseEntity.ok(GeneralResponse.builder().status(httpStatus).body(body).build());
    }

}
