package com.worldline.eyar.controller.authentication;

import com.worldline.eyar.common.authentication.AuthenticationRequest;
import com.worldline.eyar.controller.BaseController;
import com.worldline.eyar.service.authentication.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping(AuthenticationController.AUTHENTICATION_CONTEXT_URL)
@AllArgsConstructor
public class AuthenticationController extends BaseController {

    public static final String AUTHENTICATION_CONTEXT_URL = "/auth";
    private static final String LOGIN_URL = "/login";
    private static final String AUTHENTICATE_URL = "/authenticate";
    private static final String USERNAME_PARAM = "username";
    private static final String PASSWORD_PARAM = "password";
    private static final String USERNAME_VALIDATION_MSG = "username must be determined.";
    private static final String PASSWORD_VALIDATION_MSG = "password must be determined.";

    private final AuthenticationService authenticationService;

    @PostMapping(LOGIN_URL)
    public ResponseEntity<?> login(@NotBlank(message = USERNAME_VALIDATION_MSG) @RequestParam(USERNAME_PARAM) String username,
                                   @NotBlank(message = PASSWORD_VALIDATION_MSG) @RequestParam(PASSWORD_PARAM) String password) {
        return okResponse(authenticationService.login(username, password));
    }

    @RequestMapping(value = AUTHENTICATE_URL, method = RequestMethod.POST)
    public ResponseEntity<?> authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return okResponse(authenticationService.authenticate(authenticationRequest));
    }

}