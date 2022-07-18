package com.example;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends ScooterRestClient {

    private static final String INGREDIENTS_PATH_GET = "https://stellarburgers.nomoreparties.site/api/ingredients";

    private static final String ORDER_PATH = "https://stellarburgers.nomoreparties.site/api/orders";

    @Step("Get ingredients")
    public ValidatableResponse get(String token)
    {
        return given()
                .header("authorization",token)
                .when()
                .get(INGREDIENTS_PATH_GET)
                .then();
    }

    @Step("Create order with parameters {courier}")
    public ValidatableResponse createWithIngredients(Order order, String token)
    {
        return given()
                .spec(getBaseSpec())
                .header("authorization",token)
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Create order with parameters {courier}")
    public ValidatableResponse createWithoutIngredients(Order order)
    {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Get order ")
    public ValidatableResponse getOrderListWithToken(String token)
    {
        return given()
                .spec(getBaseSpec())
                .header("authorization",token)
                .when()
                .get(ORDER_PATH)
                .then();
    }

    @Step("Get order ")
    public ValidatableResponse getOrderListWithoutToken()
    {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .then();
    }
}
