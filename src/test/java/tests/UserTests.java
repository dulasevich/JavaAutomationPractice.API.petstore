package tests;

import controller.ApiResponse;
import controller.UserController;
import io.restassured.response.Response;
import models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static constants.ApiConstants.DEFAULT_USER;
import static constants.ApiConstants.USER_TO_UPDATE;

public class UserTests {
    private final UserController userController = new UserController();

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

        Response response = userController.getUserByName(DEFAULT_USER.getUsername());
        User responseBody = response.getBody().as(User.class);

        Assertions.assertEquals(200, response.statusCode(), "Incorrect response  code");Assertions.assertEquals(createdUserId, responseBody.getId(), "Id is wrong");
        Assertions.assertEquals(DEFAULT_USER.getUsername(), responseBody.getUsername(), "User name is wrong'");
        Assertions.assertEquals(DEFAULT_USER.getFirstName(), responseBody.getFirstName(), "Firs tname is wrong");
        Assertions.assertEquals(DEFAULT_USER.getLastName(), responseBody.getLastName(), "Last name is wrong");
        Assertions.assertEquals(DEFAULT_USER.getEmail(), responseBody.getEmail(), "Email is wrong");
        Assertions.assertEquals(DEFAULT_USER.getPassword(), responseBody.getPassword(), "Password is wrong");
        Assertions.assertEquals(DEFAULT_USER.getPhone(), responseBody.getPhone(), "Phone is wrong");
        Assertions.assertEquals(1, responseBody.getUserStatus(), "Status is wrong");
    }

    @Test
    void createUserTest() {
        Response addUserResponse = userController.addUser(DEFAULT_USER);
        Assertions.assertEquals(200, addUserResponse.statusCode(), "Incorrect response code");

        Response getUserResponse = userController.getUserByName(DEFAULT_USER.getUsername());
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
        Assertions.assertEquals("unknown", responseBody.getType(), "Type is wrong");
        Assertions.assertTrue(Long.parseLong(responseBody.getMessage()) > DEFAULT_USER.getId(),
                "Message is wrong");
    }

    @Test
    void updateUserTest() {
        userController.addUser(USER_TO_UPDATE);
        User updatedUser = USER_TO_UPDATE.toBuilder()
                .firstName("NewFirstName")
                .build();

        Response updateUserResponse = userController.updateUser(updatedUser);
        Assertions.assertEquals(200, updateUserResponse.statusCode(), "Incorrect response code");

        Response getUserResponse = userController.getUserByName(updatedUser.getUsername());
        User actualUser = getUserResponse.getBody().as(User.class);

        Assertions.assertEquals(200, getUserResponse.statusCode(), "Code is wrong");
        Assertions.assertEquals(updatedUser, actualUser, String.format("Expected user - {%s}, actual user - {%s}",
                updatedUser, actualUser));
    }

    @Test
    void updateUserResponseTest() {
        userController.addUser(USER_TO_UPDATE);
        User updatedUser = USER_TO_UPDATE.toBuilder()
                .firstName("NewFirstName")
                .build();

        long userToUpdateId = Long.parseLong(userController.addUser(updatedUser).as(ApiResponse.class).getMessage());
        updatedUser.setId(userToUpdateId);

        Response updateUserResponse = userController.updateUser(updatedUser);
        Assertions.assertEquals(200, updateUserResponse.statusCode(), "Incorrect response code");

        ApiResponse responseBody = updateUserResponse.getBody().as(ApiResponse.class);
        Assertions.assertEquals(200, responseBody.getCode(), "Code is wrong");
        Assertions.assertEquals("unknown", responseBody.getType(), "Type is wrong");
        Assertions.assertEquals(userToUpdateId, Long.parseLong(responseBody.getMessage()), "Message is wrong");
    }

    @Test
    void deleteUserTest() {
        userController.addUser(DEFAULT_USER);

        Response response = userController.deleteUser(DEFAULT_USER);
        Assertions.assertEquals(200, response.statusCode(), "Status code is wrong");

        ApiResponse responseBody = response.getBody().as(ApiResponse.class);
        Assertions.assertEquals(200, responseBody.getCode(), "Code is wrong");
        Assertions.assertEquals("unknown", responseBody.getType(), "Type is wrong");
        Assertions.assertEquals(DEFAULT_USER.getUsername(), responseBody.getMessage(), "Message is wrong");

        Assertions.assertEquals(404, userController.getUserByName(DEFAULT_USER.getUsername())
                .statusCode(), "Status code is wrong");
    }
}
