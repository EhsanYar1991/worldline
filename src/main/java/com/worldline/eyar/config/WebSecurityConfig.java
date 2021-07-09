package com.worldline.eyar.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_URL = "/login";

    private static final String[] swaggerUrls = {
            "/swagger-ui.html/**",
            "/webjars/**",
            "/configuration/**",
            "/swagger-resources/**",
            "/v2/api-docs"};

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic()
        .and().formLogin().successForwardUrl("/swagger/ui")
        ;
//        http
//                .authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                .exceptionHandling(e -> e
//                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
//                )
//                .httpBasic()
//                .and()
//                .formLogin()
//        ;
    }
    @Override
    public void configure(WebSecurity web) {
        web.ignoring();
    }

    @Override
    @Bean
    @Primary
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
