package constants;

import models.pet.PetStatus;
import models.User;
import models.pet.Category;
import models.pet.Pet;
import models.pet.Tags;

import java.util.List;

public class ApiConstants {
    public static final String BASE_URL = "https://petstore.swagger.io/v2/";
    public static final String API_RESPONSE_TYPE = "unknown";
    public static final User DEFAULT_USER = User.builder()
            .id(0)
            .username("lh44")
            .firstName("lewis")
            .lastName("hamilton")
            .email("l.ham@exaample.com")
            .password("pass1234")
            .phone("1234567890")
            .userStatus(1)
            .build();
    public static final User USER_TO_UPDATE = new User(0, "l4", "lando", "norris",
            "l.nor@exaample.com", "pass1234", "1234567890", 1);
    public static final Pet DEFAULT_PET = new Pet(133, new Category(2, "cat"), "dog",
            List.of("Url"), List.of(new Tags(2, "cat")), PetStatus.AVAILABLE);
    public static final Pet PET_TO_UPDATE = Pet.builder()
            .id(134)
            .status(PetStatus.AVAILABLE)
            .build();
}
