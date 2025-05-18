package tests;

import controller.ApiResponse;
import controller.UserController;
import io.restassured.response.Response;
import models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static constants.ApiConstants.DEFAULT_USER;

public class UserTests {
    private final UserController userController = new UserController();
    private static final int PERMANENT_USERNAME_ID = 2020; //from pet store example
    private static final String PERMANENT_USERNAME = "user1"; //from pet store example

    @Test
    void getUserTest() {
        Response response = userController.getUserByName(PERMANENT_USERNAME);
        User responseBody = response.getBody().as(User.class);

        Assertions.assertAll(
                () -> Assertions.assertEquals(200, response.statusCode(), "Incorrect response  code"),
                () -> Assertions.assertEquals(PERMANENT_USERNAME_ID, responseBody.getId()),
                () -> Assertions.assertEquals(PERMANENT_USERNAME, responseBody.getUsername()),
                () -> Assertions.assertEquals("Rishi", responseBody.getFirstName()),
                () -> Assertions.assertEquals("Yadav", responseBody.getLastName()),
                () -> Assertions.assertEquals("ry18@gmail.com", responseBody.getEmail()),
                () -> Assertions.assertEquals("Pass1234", responseBody.getPassword()),
                () -> Assertions.assertEquals("9638521470", responseBody.getPhone()),
                () -> Assertions.assertEquals(0, responseBody.getUserStatus())
        );
    }

    @Test
    void createUserTest() {
        Response addUserResponse = userController.addUser(DEFAULT_USER);
        Assertions.assertEquals(200, addUserResponse.statusCode(), "Incorrect response code");

        Response getUserResponse = userController.getUserByName(DEFAULT_USER.getUsername());
        User actualUser = getUserResponse.as(User.class);

        Assertions.assertAll(
                () -> Assertions.assertEquals(200, getUserResponse.statusCode(),
                        "Incorrect status code"),
                () -> Assertions.assertEquals(DEFAULT_USER, actualUser, "User created is wrong"),
                () -> Assertions.assertTrue(actualUser.getId() > PERMANENT_USERNAME_ID,
                        "User created id is wrong")
        );
    }

    @Test
    void createUserResponseTest() {
        Response addUserResponse = userController.addUser(DEFAULT_USER);
        Assertions.assertEquals(200, addUserResponse.statusCode(), "Incorrect response code");

        ApiResponse responseBody = addUserResponse.getBody().as(ApiResponse.class);
        Assertions.assertAll(
                () -> Assertions.assertEquals(200, responseBody.getCode(), "Code is wrong"),
                () -> Assertions.assertEquals("unknown", responseBody.getType(), "Type is wrong"),
                () -> Assertions.assertTrue(Long.parseLong(responseBody.getMessage()) > PERMANENT_USERNAME_ID,
                        "Message is wrong")
        );
    }

    @Test
    void updateUserTest() {
        User userToUpdate = User.builder().username("userToUpdateForTest").build();
        userController.addUser(userToUpdate).as(ApiResponse.class);
        User updatedUser = userToUpdate.toBuilder()
                .firstName("NewFirstName")
                .build();

        Response updateUserResponse = userController.updateUser(updatedUser);
        Assertions.assertEquals(200, updateUserResponse.statusCode(), "Incorrect response code");
        Response getUserResponse = userController.getUserByName(userToUpdate.getUsername());
        User actualUser = getUserResponse.getBody().as(User.class);

        Assertions.assertAll(
                () -> Assertions.assertEquals(200, getUserResponse.statusCode()),
                () -> Assertions.assertNotEquals(updatedUser, actualUser),
                () -> Assertions.assertNotNull(actualUser.getFirstName())
        );
    }

    @Test
    void updateUserResponseTest() {
        User userToUpdate = User.builder().username("userToUpdateForTest").build();
        long userToUpdateId = Long.parseLong(userController.addUser(userToUpdate).as(ApiResponse.class).getMessage());
        userToUpdate.setId(userToUpdateId);

        Response updateUserResponse = userController.updateUser(userToUpdate);
        Assertions.assertEquals(200, updateUserResponse.statusCode(), "Incorrect response code");
        ApiResponse responseBody = updateUserResponse.getBody().as(ApiResponse.class);
        Assertions.assertAll(
                () -> Assertions.assertEquals(200, responseBody.getCode(), "Code is wrong"),
                () -> Assertions.assertEquals("unknown", responseBody.getType(), "Type is wrong"),
                () -> Assertions.assertEquals(userToUpdateId, Long.parseLong(responseBody.getMessage()),
                        "Message is wrong")
        );
    }

    @Test
    void deleteUserTest() {
        String username = "userToDeleteForTest";
        userController.addUser(User.builder().username(username).build());
        Response response = userController.deleteUser(username);
        Assertions.assertEquals(200, response.statusCode(), "Status code is wrong");

        ApiResponse responseBody = response.getBody().as(ApiResponse.class);
        Assertions.assertEquals(200, responseBody.getCode(), "Code is wrong");
        Assertions.assertEquals("unknown", responseBody.getType(), "Type is wrong");
        Assertions.assertEquals(username, responseBody.getMessage(), "Message is wrong");
        Assertions.assertEquals(404, userController.getUserByName(username)
                .statusCode(), "Status code is wrong");
    }
}
