package tests;

import controller.PetController;
import io.restassured.response.Response;
import models.ApiResponse;
import models.pet.Pet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static constants.ApiConstants.*;

public class PetTests {
    private final PetController petController = new PetController();

    @BeforeEach
    @AfterEach
    void clearTestData() {
        petController.deletePet(DEFAULT_PET);
        petController.deletePet(PET_TO_UPDATE);
    }

    @Test
    void getPetTest() {
        petController.addPet(DEFAULT_PET);

        Response getPetResponse = petController.getPet(DEFAULT_PET);
        Assertions.assertEquals(200, getPetResponse.statusCode(), "Incorrect response  code");
        Assertions.assertEquals(DEFAULT_PET, getPetResponse.as(Pet.class), "Pet is wrong");
    }

    @Test
    void createPetTest() {
        Response createPetResponse = petController.addPet(DEFAULT_PET);

        Assertions.assertEquals(200, createPetResponse.statusCode(), "Incorrect response code");
        Assertions.assertEquals(DEFAULT_PET, createPetResponse.as(Pet.class), "Pet created is wrong");
    }

    @Test
    void updatePetTest() {
        petController.addPet(PET_TO_UPDATE);
        Pet updatedPet = PET_TO_UPDATE.toBuilder()
                .name("NewPet")
                .photoUrls(List.of("NewPetUrl"))
                .build();

        Response updatePetResponse = petController.updatePet(updatedPet);
        Assertions.assertEquals(200, updatePetResponse.statusCode(), "Incorrect response code");
        Assertions.assertEquals(updatedPet, updatePetResponse.as(Pet.class), "Pet was updated incorrectly");
    }

    @Test
    void deletePetTest() {
        Pet petCreated = petController.addPet(DEFAULT_PET).as(Pet.class);

        Response deletePetResponse = petController.deletePet(DEFAULT_PET);
        Assertions.assertEquals(200, deletePetResponse.statusCode(), "Incorrect response  code");

        ApiResponse responseBody = deletePetResponse.getBody().as(ApiResponse.class);
        Assertions.assertEquals(200,responseBody.getCode(), "Code is wrong");
        Assertions.assertEquals(API_RESPONSE_TYPE, responseBody.getType(), "Type is wrong");
        Assertions.assertEquals(String.valueOf(petCreated.getId()), responseBody.getMessage(), "Message is wrong");
        Assertions.assertEquals(404, petController.getPet(DEFAULT_PET).statusCode(), "Status code is wrong");
    }
}