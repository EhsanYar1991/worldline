package com.worldline.eyar.config;

import com.worldline.eyar.common.request.user.UserRequest;
import com.worldline.eyar.domain.entity.UserEntity;
import com.worldline.eyar.domain.enums.Authority;
import com.worldline.eyar.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@AllArgsConstructor
public class BootstrapConfig {
    private static final String ADMIN_USERNAME_KEY = "app.security.admin.username";
    private static final String ADMIN_PASSWORD_KEY = "app.security.admin.password";
    private static final String ADMIN_EMAIL_KEY = "app.security.admin.email";
    private static final String ADMIN_NAME_KEY = "app.security.admin.name";
    private static final String ADMIN_LASTNAME_KEY = "app.security.admin.lastname";

    private final UserService userService;
    private final Environment environment;

    /**
     * Initialize the rest admin user
     */
    @Bean
    public CommandLineRunner initDefaultAdminUserAndPrivilegeRunner() {
        String adminUsername = environment.getRequiredProperty(ADMIN_USERNAME_KEY);
        return args -> {
            UserEntity adminUser = userService.getUserByUsername(adminUsername);
            if (adminUser == null) {
                UserRequest adminRequest = new UserRequest();
                adminRequest.setAuthority(Authority.ADMIN);
                adminRequest.setUsername(adminUsername);
                adminRequest.setPassword(environment.getRequiredProperty(ADMIN_PASSWORD_KEY));
                adminRequest.setEmail(environment.getRequiredProperty(ADMIN_EMAIL_KEY));
                adminRequest.setName(environment.getRequiredProperty(ADMIN_NAME_KEY));
                adminRequest.setLastname(environment.getRequiredProperty(ADMIN_LASTNAME_KEY));
                userService.add(adminRequest);
            } else if (!adminUser.isEnabled()) {
                userService.activation(adminUser.getId(), Boolean.TRUE);
            }
        };
    }

}
