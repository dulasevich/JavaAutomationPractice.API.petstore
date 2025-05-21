package controller;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.pet.Pet;

import static constants.ApiConstants.BASE_URL;
import static io.restassured.RestAssured.given;

public class PetController {
    private static final String PET_ENDPOINT = "pet/";
    RequestSpecification requestSpecification;

    public PetController() {
        requestSpecification = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .filter(new AllureRestAssured());
    }

    public Response getPet(Pet pet) {
        return given(requestSpecification)
                .when()
                .get(PET_ENDPOINT + pet.getId());
    }

    public Response addPet(Pet pet) {
        return given(requestSpecification)
                .body(pet)
                .when()
                .post(PET_ENDPOINT);
    }

    public Response updatePet(Pet pet) {
        return given(requestSpecification)
                .body(pet)
                .when()
                .put(PET_ENDPOINT);
    }

    public Response deletePet(Pet pet) {
        return given(requestSpecification)
                .when()
                .delete(PET_ENDPOINT + pet.getId());
    }
}