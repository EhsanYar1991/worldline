package com.worldline.eyar.common.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    @NotBlank(message = "username must be determined.")
    private String username;

    @NotBlank(message = "username must be determined.")
    private String password;

    @NotBlank(message = "email must be determined.")
    @Email(message = "email format is not valid.", regexp = EMAIL_REGEX)
    private String email;

    @NotBlank(message = "name must be determined.")
    private String name;

    @NotBlank(message = "lastname must be determined.")
    private String lastname;
}
