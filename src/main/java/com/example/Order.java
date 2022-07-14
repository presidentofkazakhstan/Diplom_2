package com.example;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class Order extends ScooterRestClient {

    private ArrayList<String> ingredients = new ArrayList<>();

    private String orderId;
    private static final String INGREDIENTS_PATH_GET = "https://stellarburgers.nomoreparties.site/api/ingredients";

    private static final String ORDER_PATH = "https://stellarburgers.nomoreparties.site/api/orders";

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public ArrayList getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredient) {
        ingredients.add(ingredient);
    }
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
