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

public class UpdateUserTest {

    RandomGenerator userGenerator;
    UserClient userClient;
    User user;
    UserCredentials credentials;
    String accessToken;

    @Before
    public void setUp() {
        userGenerator = new RandomGenerator();
        userClient = new UserClient();
        user = new User(userGenerator.userEmail, userGenerator.userPassword, userGenerator.userName);
        ValidatableResponse createResponse = userClient.create(user);
        accessToken = createResponse.extract().path("accessToken");

        credentials = new UserCredentials(userGenerator.userEmail, userGenerator.userPassword);
    }

    @After
    public void tearDown() {
        userClient.delete(accessToken);
    }


    @Test
    @DisplayName("Update user without token")
    public void userUpdateWithoutToken() {
        ValidatableResponse updateResponse = userClient.update(credentials);
        int statusCode = updateResponse.extract().statusCode();
        boolean isSuccess = updateResponse.extract().path("success");

        assertThat("User update", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("User update", isSuccess, is((false)));
    }

    @Test
    @DisplayName("Update user with token")
    public void userUpdateWithValidToken() {
        ValidatableResponse updateResponse = userClient.updateWithAuth(credentials, accessToken);
        int statusCode = updateResponse.extract().statusCode();
        boolean isSuccess = updateResponse.extract().path("success");
        assertThat("User cannot update", statusCode, equalTo(SC_OK));
        assertThat("User cannot update", isSuccess, is(not(false)));
    }
}
