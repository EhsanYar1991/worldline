package com.worldline.eyar.common.request.user;

import com.worldline.eyar.domain.enums.Authority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private Long id;

    @NotBlank(message = "username must be determined.")
    private String username;

    @NotBlank(message = "username must be determined.")
    private String password;

    @NotNull(message = "authority must be determined.")
    private Authority authority;

    @NotBlank(message = "email must be determined.")
    private String email;

    @NotBlank(message = "name must be determined.")
    private String name;

    @NotBlank(message = "lastname must be determined.")
    private String lastname;

}
