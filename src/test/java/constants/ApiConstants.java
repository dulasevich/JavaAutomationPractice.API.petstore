package constants;

import models.User;

public class ApiConstants {
    public static final String BASE_URL = "https://petstore.swagger.io/v2/";
    public static final User DEFAULT_USER = new User(0, "lh44", "lewis", "hamilton",
            "l.ham@exaample.com", "pass1234", "1234567890", 1);
    public static final User USER_TO_UPDATE = new User(0, "l4", "lando", "norris",
            "l.nor@exaample.com", "pass1234", "1234567890", 1);
}
