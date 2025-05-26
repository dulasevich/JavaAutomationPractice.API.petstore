package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.FileUtil;

import java.io.File;

public class DownloadTests {

    @Test
    void downloadImageTest() {
        String endpoint = "https://bonigarcia.dev/selenium-webdriver-java/docs/webdrivermanager.png";
        Response response = FileUtil.sendUploadFileRequest(endpoint, "image/png");
        File file = FileUtil.saveFile(response);

        Assertions.assertTrue(file.exists(), "File not found");
        Assertions.assertEquals(8431, file.length(), "File size is wrong");
    }
}
