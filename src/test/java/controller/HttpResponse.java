package controller;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class HttpResponse {
    private final ValidatableResponse response;

    public HttpResponse(ValidatableResponse response) {
        this.response = response;
    }

    @Step("Check status code")
    public HttpResponse statusCodeIs(int status) {
        response.statusCode(status);
        return this;
    }

    @Step("Check json value by path '{path}' and expected value '{expectedValue}'")
    public HttpResponse jsonValueIs(String path, String expectedValue) {
        String actualValue = response.extract().jsonPath().getString(path);
        Assertions.assertEquals(expectedValue, actualValue, String.format("Actual value '%s' is not equal" +
                " to expected '%s' for the path '%s'", actualValue, expectedValue, path));
        return this;
    }

    @Step("Check json value by path '{path}' and expected value '{expectedValue}'")
    public <T> HttpResponse jsonValueIs(String path, List<T> expectedValue, Class<T> actualclass) {
        List<T> actualValue = response.extract().jsonPath().getList(path, actualclass);
        Assertions.assertEquals(expectedValue, actualValue, String.format("Actual value '%s' is not equal" +
                " to expected '%s' for the path '%s'", actualValue, expectedValue, path));
        return this;
    }

    @Step("Check json value is not NULL")
    public HttpResponse jsonValueNotNull(String path) {
        String actualValue = response.extract().jsonPath().getString(path);
        Assertions.assertNotNull(actualValue);
        return this;
    }

    @Step("Check json value is NULL")
    public HttpResponse jsonValueIsNull(String path) {
        String actualValue = response.extract().jsonPath().getString(path);
        Assertions.assertNull(actualValue);
        return this;
    }

    @Step("Check json value is bigger than {value}")
    public HttpResponse jsonValueBiggerThan(String path, long value) {
        long actualValue = response.extract().jsonPath().getLong(path);
        Assertions.assertTrue(actualValue > value, String.format("Actual value '%s' of id created " +
                "should be bigger than default one '%s'", actualValue, value));
        return this;
    }

    @Step("Check json value contains all parts {value}")
    public void jsonValueContains(String path, Object... values) {
        String actualValue = response.extract().jsonPath().getString(path);

        for (Object value : values) {
            String part = String.valueOf(value);
            Assertions.assertTrue(
                    actualValue != null && actualValue.contains(part),
                    String.format("Expected '%s' to contain '%s'", actualValue, part)
            );
        }
    }

    @Step("Get json value by path: {path}")
    public String getJsonValue(String path) {
        return response.extract().jsonPath().getString(path);
    }
}
