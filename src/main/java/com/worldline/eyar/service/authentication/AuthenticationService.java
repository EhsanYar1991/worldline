package com.worldline.eyar.service.authentication;

import com.worldline.eyar.common.authentication.AuthenticationRequest;
import com.worldline.eyar.common.response.authentication.AuthenticationResponse;
import com.worldline.eyar.common.response.user.UserResponse;
import com.worldline.eyar.config.security.JwtTokenUtil;
import com.worldline.eyar.exception.BusinessException;
import com.worldline.eyar.service.BaseService;
import com.worldline.eyar.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService extends BaseService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    public AuthenticationResponse login(String username, String password) {
        return authenticate(new AuthenticationRequest(username, password));
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws BusinessException {

        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            final String jwt = jwtTokenUtil.generateToken((UserDetails) authenticate.getPrincipal());
            return AuthenticationResponse.builder().token(jwt).build();
        } catch (DisabledException e) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "User is disabled", e);
        } catch (BadCredentialsException e) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Invalid Credential", e);
        }
    }

    public UserResponse getCurrentUserInfo() {
        return userService.makeResponse(getCurrentUser());
    }
}
