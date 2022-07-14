package com.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class LoginUserTest {
    UserCredentials credentials;
    UserCredentials credentialsWithInvalidPassword;

    RandomGenerator userGenerator;
    UserClient userClient;
    User user;
    String invalidPassword = "invalidPassword";

    String accessToken;

    @Before
    public void setUp() {
        userGenerator = new RandomGenerator();
        userClient = new UserClient();
        user = new User(userGenerator.userEmail, userGenerator.userPassword, userGenerator.userName);

        ValidatableResponse createUserResponse = userClient.create(user);
        accessToken = createUserResponse.extract().path("accessToken");

        credentials = new UserCredentials(user.getEmail(), user.getPassword());

        credentialsWithInvalidPassword = new UserCredentials(user.getEmail(), invalidPassword);
    }

    @After
    public void tearDown() {
        userClient.delete(accessToken);
    }


    @Test
    @DisplayName("Login user with valid credential")
    public void userLoginWithValidCredential() {
        ValidatableResponse loginResponse = userClient.login(credentials);
        int statusCode = loginResponse.extract().statusCode();
        boolean isSuccess = loginResponse.extract().path("success");
        assertThat("User cannot login", statusCode, equalTo(SC_OK));
        assertThat("User cannot create", isSuccess, is(not(false)));
    }

    @Test
    @DisplayName("Login user with invalid credential")
    public void userLoginWithInValidCredential() {
        ValidatableResponse loginResponse = userClient.login(credentialsWithInvalidPassword);
        int statusCode = loginResponse.extract().statusCode();
        boolean isSuccess = loginResponse.extract().path("success");
        assertThat("User login", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("User login", isSuccess, is((false)));
    }
}
