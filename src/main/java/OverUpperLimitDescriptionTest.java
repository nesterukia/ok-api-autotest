import enums.Descriptions;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class OverUpperLimitDescriptionTest extends BaseTest{
    String description = Descriptions.VOL_1025.text;
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
    public void overUpperLimitDescriptionTest() {
        given()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .when()
                .post("fb.do?method="+method+requestBody)
                .then()
                .statusCode(414)
                .log().all();
    }
}
