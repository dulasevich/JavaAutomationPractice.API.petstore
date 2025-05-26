package tests;

import controller.PetController;
import models.pet.Pet;
import models.pet.PetStatus;
import models.pet.Tags;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collections;
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
        petController.addPet(DEFAULT_PET).statusCodeIs(200);

        petController.getPet(DEFAULT_PET)
                .statusCodeIs(200)
                .jsonValueIs("id", String.valueOf(DEFAULT_PET.getId()))
                .jsonValueIs("category.id", String.valueOf(DEFAULT_PET.getCategory().getId()))
                .jsonValueIs("category.name", DEFAULT_PET.getCategory().getName())
                .jsonValueIs("name", DEFAULT_PET.getName())
                .jsonValueIs("photoUrls", DEFAULT_PET.getPhotoUrls(), String.class)
                .jsonValueIs("tags", DEFAULT_PET.getTags(), Tags.class)
                .jsonValueIs("status", String.valueOf(DEFAULT_PET.getStatus()));
    }

    @Test
    void createPetTest() {
        petController.addPet(DEFAULT_PET)
                .statusCodeIs(200)
                .jsonValueIs("id", String.valueOf(DEFAULT_PET.getId()))
                .jsonValueIs("category.id", String.valueOf(DEFAULT_PET.getCategory().getId()))
                .jsonValueIs("category.name", DEFAULT_PET.getCategory().getName())
                .jsonValueIs("name", DEFAULT_PET.getName())
                .jsonValueIs("photoUrls", DEFAULT_PET.getPhotoUrls(), String.class)
                .jsonValueIs("tags", DEFAULT_PET.getTags(), Tags.class)
                .jsonValueIs("status", String.valueOf(DEFAULT_PET.getStatus()));
    }

    @Test
    void updatePetTest() {
        petController.addPet(PET_TO_UPDATE).statusCodeIs(200);
        Pet updatedPet = PET_TO_UPDATE.toBuilder()
                .name("NewPet")
                .photoUrls(List.of("NewPetUrl"))
                .status(PetStatus.PENDING)
                .build();

        petController.updatePet(updatedPet)
                .statusCodeIs(200)
                .jsonValueIs("id", String.valueOf(updatedPet.getId()))
                .jsonValueIsNull("category.id")
                .jsonValueIsNull("category.name")
                .jsonValueIs("name", updatedPet.getName())
                .jsonValueNotNull("photoUrls")
                .jsonValueIs("tags", Collections.emptyList(), Tags.class)
                .jsonValueIs("status", String.valueOf(updatedPet.getStatus()));
    }

    @Test
    void deletePetTest() {
        petController.addPet(DEFAULT_PET).statusCodeIs(200);

        petController.deletePet(DEFAULT_PET)
                .statusCodeIs(200)
                .jsonValueIs("code", "200")
                .jsonValueIs("type", API_RESPONSE_TYPE)
                .jsonValueBiggerThan("message", DEFAULT_USER.getId());
        petController.getPet(DEFAULT_PET).statusCodeIs(404);
    }

    @Test
    void uploadImageTest() {
        petController.addPet(DEFAULT_PET).statusCodeIs(200);

        File file = new File("src/test/resources/hello-world.png");

        petController.uploadImage(DEFAULT_PET) //not putting real DEFAULT_PET id since server accepts only integer
                .statusCodeIs(200)
                .jsonValueIs("code", "200")
                .jsonValueIs("type", API_RESPONSE_TYPE)
                .jsonValueContains("message", file.getName(), file.length());
    }
}