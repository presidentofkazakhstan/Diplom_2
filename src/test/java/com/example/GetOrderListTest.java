package com.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class GetOrderListTest {
    RandomGenerator userGenerator;
    UserClient userClient;
    User user;

    OrderClient orderClient;
    Order order;
    String accessToken;

    String orderId;
    @Before
    public void setUp() {
        userGenerator = new RandomGenerator();
        userClient = new UserClient();
        order = new Order();
        orderClient= new OrderClient();
        user = new User(userGenerator.userEmail, userGenerator.userPassword, userGenerator.userName);
        ValidatableResponse createUserResponse = userClient.create(user);
        accessToken = createUserResponse.extract().path("accessToken");
        ValidatableResponse createResponse = orderClient.createWithIngredients(order, accessToken);
        orderId = createResponse.extract().path("order._id");
    }

    @After
    public void tearDown() {
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Get Order With Token")
    public void getOrderWithToken() {
        ValidatableResponse getResponse = orderClient.getOrderListWithToken(accessToken);
        int statusCode = getResponse.extract().statusCode();
        boolean isSuccess = getResponse.extract().path("success");
        String getOrderId = getResponse.extract().path("orders._id[0]");
        assertThat("", statusCode, equalTo(SC_OK));
        assertThat("", isSuccess, is(true));
        assertThat("", getOrderId, equalTo(orderId));
    }

    @Test
    @DisplayName("Get Order Without Token")
    public void getOrderWithoutToken() {
        ValidatableResponse getResponse = orderClient.getOrderListWithoutToken();
        int statusCode = getResponse.extract().statusCode();
        boolean isSuccess = getResponse.extract().path("success");
        assertThat("", statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat("", isSuccess, is((false)));

    }
}
