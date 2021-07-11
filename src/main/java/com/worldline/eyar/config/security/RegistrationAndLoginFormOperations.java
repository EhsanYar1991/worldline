package com.worldline.eyar.config.security;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Multimap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiListingBuilder;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiListing;
import springfox.documentation.service.Operation;
import springfox.documentation.spi.service.contexts.Defaults;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;
import springfox.documentation.spring.web.scanners.ApiDescriptionReader;
import springfox.documentation.spring.web.scanners.ApiListingScanner;
import springfox.documentation.spring.web.scanners.ApiListingScanningContext;
import springfox.documentation.spring.web.scanners.ApiModelReader;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class RegistrationAndLoginFormOperations extends ApiListingScanner {

    @Autowired
    private TypeResolver typeResolver;


    public RegistrationAndLoginFormOperations(ApiDescriptionReader apiDescriptionReader, ApiModelReader apiModelReader, DocumentationPluginsManager pluginsManager) {
        super(apiDescriptionReader, apiModelReader, pluginsManager);
    }

    @Override
    public Multimap<String, ApiListing> scan(ApiListingScanningContext context) {
        final Multimap<String, ApiListing> def = super.scan(context);
        def.put("authorization", new ApiListingBuilder(context.getDocumentationContext().getApiDescriptionOrdering())
                .apis(
                        Collections.singletonList(
                                new ApiDescription("Authentication", "/auth/login", "Authentication documentation",
                                        Collections.singletonList(authenticationOperation()),
                                        false)))
                .description("Custom authentication")
                .build());

        def.put("registration", new ApiListingBuilder(context.getDocumentationContext().getApiDescriptionOrdering())
                .apis(
                        Collections.singletonList(
                                new ApiDescription("Registration", "/auth/register", "Registration documentation",
                                        Collections.singletonList(registrationOperation()),
                                        false)))
                .description("Custom registration")
                .build());


        return def;
    }

    private Operation authenticationOperation() {
        return new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.POST)
                .summary("Login Api")
                .uniqueId("login")
                .parameters(Arrays.asList(new ParameterBuilder()
                                .name("username")
                                .description("The username")
                                .parameterType("query")
                                .type(typeResolver.resolve(String.class))
                                .modelRef(new ModelRef("string"))
                                .build(),
                        new ParameterBuilder()
                                .name("password")
                                .description("The password")
                                .parameterType("query")
                                .type(typeResolver.resolve(String.class))
                                .modelRef(new ModelRef("string"))
                                .build()))
                .summary("Log in") //
                .notes("Here you can log in")
                .responseMessages(
                        new HashSet<>(new Defaults().defaultResponseMessages().get(RequestMethod.POST))
                )
                .responseModel(new ModelRef("string"))
                .build();
    }

    private Operation registrationOperation() {
        return new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.POST)
                .summary("Registration Api")
                .uniqueId("register")
                .parameters(Arrays.asList(new ParameterBuilder()
                                .name("username")
                                .description("The username")
                                .parameterType("query")
                                .type(typeResolver.resolve(String.class))
                                .modelRef(new ModelRef("string"))
                                .build(),
                        new ParameterBuilder()
                                .name("password")
                                .description("The password")
                                .parameterType("query")
                                .type(typeResolver.resolve(String.class))
                                .modelRef(new ModelRef("string"))
                                .build(),
                        new ParameterBuilder()
                                .name("email")
                                .description("The email")
                                .parameterType("query")
                                .type(typeResolver.resolve(String.class))
                                .modelRef(new ModelRef("string"))
                                .build(),
                        new ParameterBuilder()
                                .name("name")
                                .description("The name")
                                .parameterType("query")
                                .type(typeResolver.resolve(String.class))
                                .modelRef(new ModelRef("string"))
                                .build(),
                        new ParameterBuilder()
                                .name("lastname")
                                .description("The lastname")
                                .parameterType("query")
                                .type(typeResolver.resolve(String.class))
                                .modelRef(new ModelRef("string"))
                                .build()
                        )
                )
                .summary("Registration") //
                .notes("Here you can register")
                .responseMessages(
                        new HashSet<>(new Defaults().defaultResponseMessages().get(RequestMethod.POST))
                )
                .responseModel(new ModelRef("string"))
                .build();
    }

}