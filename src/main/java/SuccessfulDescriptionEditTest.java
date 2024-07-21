import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;


public class SuccessfulDescriptionEditTest extends BaseTest{
    String description = "новое описание";
    String method = "photos.editPhoto";

    @BeforeEach
    public void init() {
        headers
                .put("description", description)
                .put("method", method);
        super.init();
    }

    @Test
    public void successfulDescriptionEditTest() {
        given()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .when()
                .post("fb.do?method="+method+requestBody)
                .then()
                .statusCode(200)
                .log().all();
    }
}
