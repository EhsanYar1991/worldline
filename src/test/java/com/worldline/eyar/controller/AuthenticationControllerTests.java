package com.worldline.eyar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.worldline.eyar.common.GeneralResponse;
import com.worldline.eyar.common.response.authentication.AuthenticationResponse;
import com.worldline.eyar.common.response.user.UserResponse;
import com.worldline.eyar.domain.enums.Authority;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTests {

    private static final String USERNAME = "eyar";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "eyar@domain.com";
    private static final String NAME = "Ehsan";
    private static final String LASTNAME = "Yar";


    @Value("${app.security.admin.username}")
    private String adminUsername;

    @Value("${app.security.admin.password}")
    private String adminPassword;

    private static String adminToken;
    private static String userToken;


    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    public void loginWithAdminTest() throws Exception {
        mockMvc.perform(post("/auth/login")
                .queryParam("username", adminUsername)
                .queryParam("password", adminPassword))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(result -> {
                    GeneralResponse response =
                            OBJECT_MAPPER.readValue(result.getResponse().getContentAsByteArray(), GeneralResponse.class);
                    Assertions.assertNotNull(response);
                    Assertions.assertEquals(response.getStatus(), HttpStatus.OK);
                    Assertions.assertNotNull(response.getBody());
                    AuthenticationResponse authenticationResponse =
                            OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(response.getBody()), AuthenticationResponse.class);
                    Assertions.assertNotNull(authenticationResponse.getToken());
                    adminToken = String.format("Bearer %s", authenticationResponse.getToken());
                });
    }

    public void registerWithUserTest() throws Exception {
        MvcResult registerResult = mockMvc.perform(post("/auth/register")
                .queryParam("username", USERNAME)
                .queryParam("password", PASSWORD)
                .queryParam("email", EMAIL)
                .queryParam("name", NAME)
                .queryParam("lastname", LASTNAME))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();
        GeneralResponse registerResponse =
                OBJECT_MAPPER.readValue(registerResult.getResponse().getContentAsString(), GeneralResponse.class);
        Assertions.assertNotNull(registerResponse);
        Assertions.assertEquals(registerResponse.getStatus(), HttpStatus.OK);
        Assertions.assertNotNull(registerResponse.getBody());
        UserResponse userResponse =
                OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(registerResponse.getBody()), UserResponse.class);
        Assertions.assertNotNull(userResponse.getUsername());
        Assertions.assertNotNull(userResponse.getEmail());
        Assertions.assertNotNull(userResponse.getLastname());
        Assertions.assertNotNull(userResponse.getAuthority());
        Assertions.assertNotNull(userResponse.getName());
        Assertions.assertEquals(userResponse.getAuthority(), Authority.USER);
    }

    @Test
    public void registerAndLoginWithUserTest() throws Exception {
        registerWithUserTest();
        mockMvc.perform(post("/auth/login")
                .queryParam("username", USERNAME)
                .queryParam("password", PASSWORD))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(result -> {
                    GeneralResponse response =
                            OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), GeneralResponse.class);
                    Assertions.assertNotNull(response);
                    Assertions.assertEquals(response.getStatus(), HttpStatus.OK);
                    Assertions.assertNotNull(response.getBody());
                    AuthenticationResponse authenticationResponse =
                            OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(response.getBody()), AuthenticationResponse.class);
                    Assertions.assertNotNull(authenticationResponse.getToken());
                    userToken = String.format("Bearer %s", authenticationResponse.getToken());
                });
    }



}
