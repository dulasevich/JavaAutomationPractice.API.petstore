package controller;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import models.pet.Pet;

import java.io.File;

import static constants.ApiConstants.BASE_URL;
import static io.restassured.RestAssured.given;

public class PetController {
    private static final String PET_ENDPOINT = "pet/";
    private final RequestSpecification requestSpecification;

    public PetController() {
        requestSpecification = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .filter(new AllureRestAssured());
    }

    public HttpResponse getPet(Pet pet) {
        ValidatableResponse response = given(requestSpecification)
                .when()
                .get(PET_ENDPOINT + pet.getId())
                .then();
        return new HttpResponse(response);
    }

    public HttpResponse addPet(Pet pet) {
        ValidatableResponse response = given(requestSpecification)
                .body(pet)
                .when()
                .post(PET_ENDPOINT)
                .then();
        return new HttpResponse(response);
    }

    public HttpResponse updatePet(Pet pet) {
        ValidatableResponse response = given(requestSpecification)
                .body(pet)
                .when()
                .put(PET_ENDPOINT)
                .then();
        return new HttpResponse(response);
    }

    public HttpResponse deletePet(Pet pet) {
        ValidatableResponse response = given(requestSpecification)
                .when()
                .delete(PET_ENDPOINT + pet.getId())
                .then();
        return new HttpResponse(response);
    }

    public HttpResponse uploadImage(Pet pet) {
        ValidatableResponse response = given()
                .baseUri(BASE_URL)
                .multiPart("file", new File("src/test/resources/hello-world.png"))
                .contentType("multipart/form-data")
                .filter(new AllureRestAssured())
                .when()
                .post(PET_ENDPOINT + pet.getId() + "/uploadImage")
                .then();
        return new HttpResponse(response);
    }
}