package com.worldline.eyar.config;


import com.worldline.eyar.config.security.FormLoginOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
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
public class SwaggerConfig {

    private static final String APP_NAME_KEY = "spring.application.name";
    private static final String DESCRIPTION_KEY = "spring.application.description";
    private static final String VERSION_KEY = "spring.application.version";
    private static final String AUTHORIZATION_SCOPE = "global";
    private static final String PASS_AS_TYPE = "header";
    private static final String AUTHORIZATION_VALUE = "Authorization";
    private static final String AUTHORIZATION_SCOPE_DESCRIPTION = "accessEverything";
    private static final String CONTACT_NAME = "Ehsan Yar";
    private static final String CONTACT_URL = "ehsanyar1991@gmail.com";
    private static final String CONTACT_EMAIL = "https://github.com/EhsanYar1991";

    private final String appName;
    private final String appDescription;
    private final String version;


    public SwaggerConfig(@Autowired Environment environment) {
        this.appName = environment.getRequiredProperty(APP_NAME_KEY);
        this.appDescription = environment.getRequiredProperty(DESCRIPTION_KEY);
        this.version = environment.getRequiredProperty(VERSION_KEY);
    }


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiKey()))
                .select()
//                .apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.worldline.eyar.controller.**"))
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
                appName,
                appDescription,
                version,
                "Terms of service",
                new Contact(CONTACT_NAME, CONTACT_URL, CONTACT_EMAIL),
                "License of API",
                "API license URL",
                Collections.emptyList());
    }

    @Primary
    @Bean
    public ApiListingScanner addExtraOperations(ApiDescriptionReader apiDescriptionReader, ApiModelReader apiModelReader, DocumentationPluginsManager pluginsManager) {
        return new FormLoginOperations(apiDescriptionReader, apiModelReader, pluginsManager);
    }

}
