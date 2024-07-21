import enums.Descriptions;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class UnderUpperLimitDescriptionTest extends BaseTest{
    String description = Descriptions.VOL_1023.text;
    String method = "photos.editPhoto";

    @BeforeEach
    public void init() {
        headers
                .put("description", description)
                .put("method", method);
        super.init();
    }
    @Test
    @Tag("UpperLimit")
    public void underUpperLimitDescriptionTest() {
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
