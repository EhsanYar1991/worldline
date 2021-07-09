package com.worldline.eyar.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

@Getter
@AllArgsConstructor
@ToString
public enum Authority implements GrantedAuthority {

    ADMIN("ADMIN"),
    USER("USER");

    private String authority;

}
