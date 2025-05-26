package tests;

import controller.UserController;
import models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static constants.ApiConstants.*;

public class UserTests {
    private final UserController userController = new UserController();
    private static final String NEW_FIRST_NAME = "NewFirstName";

    @BeforeEach
    @AfterEach
    void clearTestData() {
        userController.deleteUser(DEFAULT_USER);
        userController.deleteUser(USER_TO_UPDATE);
    }

    @Test
    void getUserTest() {
        String createdUserId = userController.addUser(DEFAULT_USER).statusCodeIs(200).getJsonValue("message");

        userController.getUser(DEFAULT_USER)
                .statusCodeIs(200)
                .jsonValueIs("id", createdUserId)
                .jsonValueIs("username", DEFAULT_USER.getUsername())
                .jsonValueIs("firstName", DEFAULT_USER.getFirstName())
                .jsonValueIs("lastName", DEFAULT_USER.getLastName())
                .jsonValueIs("email", DEFAULT_USER.getEmail())
                .jsonValueIs("password", DEFAULT_USER.getPassword())
                .jsonValueIs("phone", DEFAULT_USER.getPhone())
                .jsonValueIs("userStatus", String.valueOf(DEFAULT_USER.getUserStatus()));
    }

    @Test
    void createUserResponseTest() {
        userController.addUser(DEFAULT_USER)
                .statusCodeIs(200)
                .jsonValueIs("code", "200")
                .jsonValueIs("type", API_RESPONSE_TYPE)
                .jsonValueBiggerThan("message", DEFAULT_USER.getId());
    }

    @Test
    void updateUserTest() {
        String createdUserId = userController.addUser(USER_TO_UPDATE).statusCodeIs(200).getJsonValue("message");
        User updatedUser = USER_TO_UPDATE.toBuilder().firstName(NEW_FIRST_NAME).build();

        userController.updateUser(updatedUser)
                .statusCodeIs(200);

        userController.getUser(updatedUser)
                .statusCodeIs(200)
                .jsonValueIs("id", createdUserId)
                .jsonValueIs("username", updatedUser.getUsername())
                .jsonValueIs("firstName", updatedUser.getFirstName())
                .jsonValueIs("lastName", updatedUser.getLastName())
                .jsonValueIs("email", updatedUser.getEmail())
                .jsonValueIs("password", updatedUser.getPassword())
                .jsonValueIs("phone", updatedUser.getPhone())
                .jsonValueIs("userStatus", String.valueOf(updatedUser.getUserStatus()));
    }

    @Test
    void updateUserResponseTest() {
        String createdUserId = userController.addUser(USER_TO_UPDATE).statusCodeIs(200).getJsonValue("message");
        User updatedUser = USER_TO_UPDATE.toBuilder()
                .id(Long.parseLong(createdUserId))
                .firstName(NEW_FIRST_NAME)
                .build();

        userController.updateUser(updatedUser)
                .statusCodeIs(200)
                .jsonValueIs("code", "200")
                .jsonValueIs("type", API_RESPONSE_TYPE)
                .jsonValueIs("message", createdUserId);
    }

    @Test
    void deleteUserTest() {
        userController.addUser(DEFAULT_USER);
        userController.deleteUser(DEFAULT_USER)
                .statusCodeIs(200)
                .jsonValueIs("code", "200")
                .jsonValueIs("type", API_RESPONSE_TYPE)
                .jsonValueIs("message", DEFAULT_USER.getUsername());
        userController.getUser(DEFAULT_USER).statusCodeIs(404);
    }
}
