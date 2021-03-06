package com.worldline.eyar.common.request.user;

import com.worldline.eyar.domain.enums.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    private static final String EMAIL_REGEX = "(^[A-Za-z0-9+_.-]+@(.+)$)";

    private Long id;

    @NotBlank(message = "username must be determined.")
    private String username;

    @NotBlank(message = "username must be determined.")
    private String password;

    @NotNull(message = "authority must be determined.")
    private Authority authority;

    @NotBlank(message = "email must be determined.")
    @Email(message = "email format is not valid.", regexp = EMAIL_REGEX)
    private String email;

    @NotBlank(message = "name must be determined.")
    private String name;

    @NotBlank(message = "lastname must be determined.")
    private String lastname;

}
