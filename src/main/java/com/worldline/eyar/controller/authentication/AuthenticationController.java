package com.worldline.eyar.controller.authentication;

import com.worldline.eyar.common.request.authentication.AuthenticationRequest;
import com.worldline.eyar.common.request.user.RegisterRequest;
import com.worldline.eyar.controller.BaseController;
import com.worldline.eyar.service.authentication.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private static final String CURRENT_USER_INFO = "/user-info";
    public static final String USER_REGISTRATION = "/register";
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

    @PostMapping(value = AUTHENTICATE_URL)
    public ResponseEntity<?> authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return okResponse(authenticationService.authenticate(authenticationRequest));
    }

    @PreAuthorize(ALL_AUTHORITY)
    @GetMapping(value = CURRENT_USER_INFO)
    public ResponseEntity<?> getCurrentUserInfo() {
        return okResponse(authenticationService.getCurrentUserInfo());
    }

    @PostMapping(USER_REGISTRATION)
    public ResponseEntity<?> register(@Valid RegisterRequest request) {
        return okResponse(authenticationService.register(request));
    }

}