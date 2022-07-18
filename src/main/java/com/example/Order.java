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

}
