package com.worldline.eyar.common.request.user;

import com.worldline.eyar.domain.enums.Authority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private Long id;
    private String username;
    private String password;
    private Authority authority;
    private String email;
    private String name;
    private String lastname;

}
