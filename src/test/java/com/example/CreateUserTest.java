package com.example;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import io.qameta.allure.junit4.DisplayName;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Test;

public class CreateUserTest {
    UserClient userClient;
    User user;

    User userWithoutPassword;
    RandomGenerator userGenerator;
    String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        userGenerator = new RandomGenerator();
        user = new User(userGenerator.userEmail, userGenerator.userPassword, userGenerator.userName);
        userWithoutPassword = new User(userGenerator.userEmail, userGenerator.userName);
    }

    @After
    public void tearDown() {
        if(accessToken != null)
            userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Create user with valid credential")
    public void userCreateWithValidCredential() {
        ValidatableResponse createResponse = userClient.create(user);
        int statusCode = createResponse.extract().statusCode();
        boolean isSuccess = createResponse.extract().path("success");
        accessToken = createResponse.extract().path("accessToken");
        assertThat("User cannot create", statusCode, equalTo(SC_OK));
        assertThat("User cannot create", isSuccess, is(not(false)));
    }

    @Test
    @DisplayName("Create user with repeated email")
    public void userCreateWithRepeatedLogin() {
        ValidatableResponse firstCreateResponse = userClient.create(user);
        ValidatableResponse secondCreateResponse = userClient.create(user);
        int statusCode = secondCreateResponse.extract().statusCode();
        String textMessage = secondCreateResponse.extract().path("message");
        accessToken = secondCreateResponse.extract().path("accessToken");
        assertThat("User created", statusCode, equalTo(SC_FORBIDDEN));
        assertThat("User created", textMessage, is("User already exists"));
    }

    @Test
    @DisplayName("Create user without password")
    public void userCreateWithoutPassword() {

        ValidatableResponse createResponse = userClient.create(userWithoutPassword);
        int statusCode = createResponse.extract().statusCode();
        String textMessage = createResponse.extract().path("message");
        accessToken = createResponse.extract().path("accessToken");
        assertThat("User created", statusCode, equalTo(SC_FORBIDDEN));
        assertThat("User created", textMessage, is("Email, password and name are required fields"));
    }
}
