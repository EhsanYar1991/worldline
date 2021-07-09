package com.worldline.eyar.domain.entity;

import com.worldline.eyar.domain.BaseEntity;
import com.worldline.eyar.domain.FieldMetaData;
import com.worldline.eyar.domain.enums.Authority;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "password")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "USER")
public class UserEntity extends BaseEntity implements UserDetails {

    @Column(name = "USERNAME", unique = true, nullable = false)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "ROLE", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Authority authority;

    @Column(name = "EMAIL",nullable = false)
    private String email;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "LASTNAME", nullable = false)
    private String lastname;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(authority);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return getActive();
    }

    public interface UserEntityFields extends BaseEntityFields{
        FieldMetaData<String> USERNAME = new FieldMetaData<>("username");
        FieldMetaData<String> PASSWORD = new FieldMetaData<>("password");
        FieldMetaData<Authority> AUTHORITY = new FieldMetaData<>("authority");
        FieldMetaData<String> EMAIL = new FieldMetaData<>("email");
        FieldMetaData<Authority> NAME = new FieldMetaData<>("name");
        FieldMetaData<Authority> LAST_NAME = new FieldMetaData<>("lastname");
    }

}

