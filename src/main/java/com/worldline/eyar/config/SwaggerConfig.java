package com.worldline.eyar.config;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
@Profile("dev")
@AllArgsConstructor
public class SwaggerConfig {

    private static final String APP_NAME_KEY = "spring.application.name";
    private static final String DESCRIPTION_KEY = "spring.application.description";
    private static final String VERSION_KEY = "spring.application.version";
    private static final String TERMS = "spring.application.version";
    private static final String LICENSE = "application.license";
    private static final String LICENSE_URL = "application.license-url";
    private static final String CONTACT_NAME = "application.contact-name";
    private static final String CONTACT_EMAIL = "application.contact-email";
    private static final String CONTACT_URL = "application.contact-url";
    private static final String SWAGGER_BASE_URL_CONTEXT = "/swagger";
    private static final String SWAGGER_UI_URL_CONTEXT = "/ui";
    private static final String SWAGGER_REDIRECTION = "redirect:swagger-ui.html";
    private static final String CONTROLLER_BASE_PACKAGE = "com.worldline.eyar.controller";

    private final Environment environment;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(CONTROLLER_BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build();
    }


    private ApiInfo apiInfo() {
        return new ApiInfo(
                environment.getRequiredProperty(APP_NAME_KEY),
                environment.getRequiredProperty(DESCRIPTION_KEY),
                environment.getRequiredProperty(VERSION_KEY),
                environment.getRequiredProperty(TERMS),
                new Contact(environment.getRequiredProperty(CONTACT_NAME),
                        environment.getRequiredProperty(CONTACT_URL),
                        environment.getRequiredProperty(CONTACT_EMAIL)),
                environment.getRequiredProperty(LICENSE),
                environment.getRequiredProperty(LICENSE_URL),
                Collections.emptyList());
    }


    @Controller
    @RequestMapping(SWAGGER_BASE_URL_CONTEXT)
    public static class SwaggerController {

        @RequestMapping(SWAGGER_UI_URL_CONTEXT)
        public String swagger() {
            return SWAGGER_REDIRECTION;
        }
    }

}
