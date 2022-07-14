package com.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class CreateOrderTest {
    RandomGenerator userGenerator;
    UserClient userClient;
    User user;

    Order order;
    String accessToken;

    @Before
    public void setUp() {
        userGenerator = new RandomGenerator();
        userClient = new UserClient();
        order = new Order();
        user = new User(userGenerator.userEmail, userGenerator.userPassword, userGenerator.userName);
        ValidatableResponse createResponse = userClient.create(user);
        accessToken = createResponse.extract().path("accessToken");
    }

    @After
    public void tearDown() {
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Create order with token and ingredients")
    public void orderCreateWithIngredients() {
        ValidatableResponse getResponse = order.get(accessToken);
        String f1 = getResponse.extract().path("data._id[0]");
        String f2 = getResponse.extract().path("data._id[1]");
        order.setIngredients(f1);
        order.setIngredients(f2);

        ValidatableResponse createResponse = order.createWithIngredients(order, accessToken);
        int statusCode = createResponse.extract().statusCode();
        boolean isSuccess = createResponse.extract().path("success");
        assertThat("Order cannot create", statusCode, equalTo(SC_OK));
        assertThat("Order cannot create", isSuccess, is(not(false)));
    }

    @Test
    @DisplayName("Create order without token and ingredients")
    public void orderCreateWithoutIngredients() {
        ValidatableResponse createResponse = order.createWithoutIngredients(order);
        int statusCode = createResponse.extract().statusCode();
        boolean isSuccess = createResponse.extract().path("success");
        assertThat("Order cannot create", statusCode, equalTo(SC_BAD_REQUEST));
        assertThat("Order cannot create", isSuccess, is((false)));
    }

    @Test
    @DisplayName("Create order with invalid ingredients")
    public void orderCreateWithInvalidIngredients() {
        order.setIngredients("InvalidIngredients");
        ValidatableResponse createResponse = order.createWithIngredients(order, accessToken);
        int statusCode = createResponse.extract().statusCode();
        assertThat("Order cannot create", statusCode, equalTo(SC_INTERNAL_SERVER_ERROR));
    }
}
