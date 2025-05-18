package controller;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.User;

import static constants.ApiConstants.BASE_URL;
import static io.restassured.RestAssured.given;

public class UserController {
    private static final String USER_ENDPOINT = "user/";
    RequestSpecification requestSpecification;

    public UserController() {
        requestSpecification = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .baseUri(BASE_URL)
                .filter(new AllureRestAssured());
    }

    public Response getUserByName(String userName) {
        int attemptNumber = 0;
        Response response = null;
        while (attemptNumber < 5) {
            response = given(requestSpecification)
                    .when()
                    .get(USER_ENDPOINT + userName);
            if (response.statusCode() == 200) {
                return response;
            }
            attemptNumber++;
        }
        return response;
    }

    public Response addUser(User user) {
        return given(requestSpecification)
                .body(user)
                .when()
                .post(USER_ENDPOINT);
    }

    public Response updateUser(User user) {
        return given(requestSpecification)
                .body(user)
                .when()
                .put(USER_ENDPOINT + user.getUsername());
    }

    public Response deleteUser(String  userName) {
        int attemptNumber = 0;
        Response response = null;
        while (attemptNumber < 5) {
            response = given(requestSpecification)
                    .when()
                    .delete(USER_ENDPOINT + userName);
            if (response.contentType() != null && response.contentType().contains("application/json")) {
                return response;
            }
            attemptNumber++;
        }
        return response;
    }
}
