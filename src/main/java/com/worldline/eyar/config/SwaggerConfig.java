package com.worldline.eyar.config;


import com.worldline.eyar.config.security.FormLoginOperations;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.scanners.ApiDescriptionReader;
import springfox.documentation.spring.web.scanners.ApiListingScanner;
import springfox.documentation.spring.web.scanners.ApiModelReader;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
@Profile("dev")
@AllArgsConstructor
public class SwaggerConfig {

    private static final String AUTHORIZATION_SCOPE = "global";
    private static final String PASS_AS_TYPE = "header";
    private static final String AUTHORIZATION_VALUE = "Authorization";
    private static final String AUTHORIZATION_SCOPE_DESCRIPTION = "accessEverything";
    private static final String APP_NAME_KEY = "spring.application.name";
    private static final String DESCRIPTION_KEY = "spring.application.description";
    private static final String VERSION_KEY = "spring.application.version";
    private static final String TERMS = "spring.application.version";
    private static final String LICENSE = "application.license";
    private static final String LICENSE_URL = "application.license-url";
    private static final String CONTACT_NAME = "application.contact-name";
    private static final String CONTACT_EMAIL = "application.contact-email";
    private static final String CONTACT_URL = "application.contact-url";
    private static final String SWAGGER_BASE_URL_CONTEXT = "/docs";
    private static final String SWAGGER_URL = "/swagger";
    private static final String SWAGGER_REDIRECTION = "redirect:swagger-ui.html";
    private static final String CONTROLLER_BASE_PACKAGE = "com.worldline.eyar.controller";

    private final Environment environment;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.basePackage(CONTROLLER_BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build();
    }


    private ApiKey apiKey() {
        return new ApiKey(AUTHORIZATION_VALUE, AUTHORIZATION_VALUE, PASS_AS_TYPE);
    }


    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        return Collections.singletonList(
                new SecurityReference(
                        AUTHORIZATION_VALUE,
                        new AuthorizationScope[]{new AuthorizationScope(AUTHORIZATION_SCOPE, AUTHORIZATION_SCOPE_DESCRIPTION)}
                )
        );
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

    @Primary
    @Bean
    public ApiListingScanner addExtraOperations(ApiDescriptionReader apiDescriptionReader, ApiModelReader apiModelReader, DocumentationPluginsManager pluginsManager) {
        return new FormLoginOperations(apiDescriptionReader, apiModelReader, pluginsManager);
    }


    @Controller
    @RequestMapping(SWAGGER_BASE_URL_CONTEXT)
    public static class SwaggerController {

        @RequestMapping(SWAGGER_URL)
        public String swagger() {
            return SWAGGER_REDIRECTION;
        }
    }

}
