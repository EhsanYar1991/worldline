package com.worldline.eyar.service;

import com.worldline.eyar.domain.entity.UserEntity;
import com.worldline.eyar.domain.enums.Authority;
import com.worldline.eyar.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.transaction.Transactional;

@Transactional(rollbackOn = Throwable.class)
public class BaseService {

    @Autowired
    protected UserService userService;

    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getName() != null ? userService.getUserByUsername(authentication.getName()) : null;
    }

    public boolean isCurrentUserAdmin(){
        UserEntity currentUser = getCurrentUser();
        return currentUser != null && Authority.ADMIN.equals(currentUser.getAuthority());
    }

}
