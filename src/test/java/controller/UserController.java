package controller;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import models.User;

import static constants.ApiConstants.BASE_URL;
import static io.restassured.RestAssured.given;

public class UserController {
    private static final String USER_ENDPOINT = "user/";
    private final RequestSpecification requestSpecification;

    public UserController() {
        requestSpecification = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .filter(new AllureRestAssured());
    }

    public HttpResponse getUser(User user) {
        ValidatableResponse response =  given(requestSpecification)
                .when()
                .get(USER_ENDPOINT + user.getUsername())
                .then();
        return new HttpResponse(response);
    }

    public HttpResponse addUser(User user) {
        ValidatableResponse response = given(requestSpecification)
                .body(user)
                .when()
                .post(USER_ENDPOINT)
                .then();
        return new HttpResponse(response);
    }

    public HttpResponse updateUser(User user) {
        ValidatableResponse response = given(requestSpecification)
                .body(user)
                .when()
                .put(USER_ENDPOINT + user.getUsername())
                .then();
        return new HttpResponse(response);
    }

    public HttpResponse deleteUser(User user) {
        ValidatableResponse response = given(requestSpecification)
                .when()
                .delete(USER_ENDPOINT + user.getUsername())
                .then();
        return new HttpResponse(response);
    }
}
