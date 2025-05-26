package utils;

import io.restassured.response.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

public class FileUtil {

    public static Response sendUploadFileRequest(String endpoint, String contentType) {
        return given()
                .when()
                .get(endpoint)
                .then()
                .contentType(contentType)
                .statusCode(200)
                .extract()
                .response();
    }

    public static File saveFile(Response response) {
        byte[] imageBytes = response.getBody().asByteArray();

        Path outputPath = Paths.get("src/test/resources/webdrivermanager.png");
        try {
            Files.write(outputPath, imageBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return outputPath.toFile();
    }
}
