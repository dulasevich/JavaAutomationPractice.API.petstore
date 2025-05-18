package constants;

import models.User;

public class ApiConstants {
    public static final String BASE_URL = "https://petstore.swagger.io/v2/";
    public static final User DEFAULT_USER = new User(0, "lh44", "lewis", "hamilton",
            "l.ham@exaample.com", "pass1234", "1234567890", 1);
}
