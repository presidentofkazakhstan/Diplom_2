package com.example;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;


public class UserClient extends ScooterRestClient {
    private static final String USER_PATH_REGISTER = "https://stellarburgers.nomoreparties.site/api/auth/register";
    private static final String USER_PATH_LOGIN = "https://stellarburgers.nomoreparties.site/api/auth/login";
    private static final String USER_PATH = "https://stellarburgers.nomoreparties.site/api/auth/user";




    @Step("Create user with parameters {courier}")
    public ValidatableResponse create(User user)
    {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH_REGISTER)
                .then();
    }


    @Step("Login user with credentials {credentials}")
    public ValidatableResponse login(UserCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(USER_PATH_LOGIN)
                .then();
    }

    @Step("Update user without token")
    public ValidatableResponse update(UserCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .patch(USER_PATH)
                .then();
    }

    @Step("Update user with token")
    public ValidatableResponse updateWithAuth(UserCredentials credentials, String token) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .header("authorization",token)
                .when()
                .patch(USER_PATH)
                .then();
    }

    @Step("DELETE user with token")
    public ValidatableResponse delete(String token) {
        return given()
                .spec(getBaseSpec())
                .header("authorization",token)
                .when()
                .delete(USER_PATH)
                .then();
    }

}
