package tests;

import models.ApiResponse;
import controller.UserController;
import io.restassured.response.Response;
import models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
        long createdUserId = Long.parseLong(userController.addUser(DEFAULT_USER)
                .as(ApiResponse.class).getMessage());

        Response getUserResponse = userController.getUser(DEFAULT_USER);
        User userCreated = getUserResponse.getBody().as(User.class);

        Assertions.assertEquals(200, getUserResponse.statusCode(), "Incorrect response  code");
        Assertions.assertEquals(createdUserId, userCreated.getId(), "Id is wrong");
        Assertions.assertEquals(DEFAULT_USER, userCreated, "User is wrong");
    }

    @Test
    void createUserTest() {
        Response addUserResponse = userController.addUser(DEFAULT_USER);
        Assertions.assertEquals(200, addUserResponse.statusCode(), "Incorrect response code");

        Response getUserResponse = userController.getUser(DEFAULT_USER);
        User actualUser = getUserResponse.as(User.class);

        Assertions.assertEquals(200, getUserResponse.statusCode(), "Incorrect status code");
        Assertions.assertEquals(DEFAULT_USER, actualUser, "User created is wrong");
        Assertions.assertTrue(actualUser.getId() > DEFAULT_USER.getId(), "User created id is wrong");
    }

    @Test
    void createUserResponseTest() {
        Response addUserResponse = userController.addUser(DEFAULT_USER);
        Assertions.assertEquals(200, addUserResponse.statusCode(), "Incorrect response code");

        ApiResponse responseBody = addUserResponse.getBody().as(ApiResponse.class);
        Assertions.assertEquals(200, responseBody.getCode(), "Code is wrong");
        Assertions.assertEquals(API_RESPONSE_TYPE, responseBody.getType(), "Type is wrong");
        Assertions.assertTrue(Long.parseLong(responseBody.getMessage()) > DEFAULT_USER.getId(),
                "Message is wrong");
    }

    @Test
    void updateUserTest() {
        userController.addUser(USER_TO_UPDATE);
        User updatedUser = USER_TO_UPDATE.toBuilder()
                .firstName(NEW_FIRST_NAME)
                .build();

        Response updateUserResponse = userController.updateUser(updatedUser);
        Assertions.assertEquals(200, updateUserResponse.statusCode(), "Incorrect response code");

        Response getUserResponse = userController.getUser(updatedUser);
        User actualUser = getUserResponse.getBody().as(User.class);

        Assertions.assertEquals(200, getUserResponse.statusCode(), "Code is wrong");
        Assertions.assertEquals(updatedUser, actualUser, String.format("Expected user - {%s} \n Actual user - {%s}",
                updatedUser, actualUser));
    }

    @Test
    void updateUserResponseTest() {
        userController.addUser(USER_TO_UPDATE);
        User updatedUser = USER_TO_UPDATE.toBuilder()
                .firstName(NEW_FIRST_NAME)
                .build();

        long userToUpdateId = Long.parseLong(userController.addUser(updatedUser).as(ApiResponse.class).getMessage());
        updatedUser.setId(userToUpdateId);

        Response updateUserResponse = userController.updateUser(updatedUser);
        Assertions.assertEquals(200, updateUserResponse.statusCode(), "Incorrect response code");

        ApiResponse responseBody = updateUserResponse.getBody().as(ApiResponse.class);
        Assertions.assertEquals(200, responseBody.getCode(), "Code is wrong");
        Assertions.assertEquals(API_RESPONSE_TYPE, responseBody.getType(), "Type is wrong");
        Assertions.assertEquals(userToUpdateId, Long.parseLong(responseBody.getMessage()), "Message is wrong");
    }

    @Test
    void deleteUserTest() {
        userController.addUser(DEFAULT_USER);

        Response deleteUserResponse = userController.deleteUser(DEFAULT_USER);
        Assertions.assertEquals(200, deleteUserResponse.statusCode(), "Status code is wrong");

        ApiResponse responseBody = deleteUserResponse.getBody().as(ApiResponse.class);
        Assertions.assertEquals(200, responseBody.getCode(), "Code is wrong");
        Assertions.assertEquals(API_RESPONSE_TYPE, responseBody.getType(), "Type is wrong");
        Assertions.assertEquals(DEFAULT_USER.getUsername(), responseBody.getMessage(), "Message is wrong");
        Assertions.assertEquals(404, userController.getUser(DEFAULT_USER).statusCode(),
                "Status code is wrong");
    }
}
