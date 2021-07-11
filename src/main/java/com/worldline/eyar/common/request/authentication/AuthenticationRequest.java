package com.worldline.eyar.common.request.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest implements Serializable {
    @NotBlank(message = "username must be determined.")
    private String username;

    @NotBlank(message = "password must be determined.")
    private String password;
}