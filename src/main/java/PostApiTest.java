import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class PostApiTest {

    public JSONObject headers = new JSONObject();
    public final String application_key = "CFAOONLGDIHBABABA";
    public final String application_secret_key = "C5601461D042CBF949FA3548";
    public final String description = "hi im java";
    public final String format = "json";
    public final String method = "photos.editPhoto";
    public final String photo_id = "970759870640";
    public final String access_token = "-n-eaiE7uRKyhtjqYEFkv58Dds0a0Dqzg4IJUKwv1tT0aADQcl8Oev8EIL5J7UU5Wv5jmIX6aXFFJarrn4Ph";
    public String requestBody;
    public String sig="";
    @BeforeEach
    public void init() throws NoSuchAlgorithmException {
        headers
                .put("application_key", application_key)
                .put("description", description)
                .put("format", format)
                .put("method", method)
                .put("photo_id", photo_id);


        MessageDigest md1 = MessageDigest.getInstance("MD5");
        String mdInput = access_token + application_secret_key;
        byte[] secretKeyBytes = md1.digest(mdInput.getBytes());
        String secretKey = new String(Hex.encodeHex(secretKeyBytes)).toLowerCase();


        ArrayList<String> keys = new ArrayList<>(headers.keySet());
        Collections.sort(keys);
        keys.forEach(keyStr ->{
            Object keyValue = headers.get(keyStr);
            sig = sig.concat(keyStr).concat("=").concat(keyValue.toString());
        });
        sig = sig.concat(secretKey);

        System.out.println(sig);
        MessageDigest md2 = MessageDigest.getInstance("MD5");
        byte[] sigBytes = md2.digest(sig.getBytes());
        String sigMD5 = new String(Hex.encodeHex(sigBytes)).toLowerCase();

        System.out.println(sigMD5);
        headers.put("sig", sigMD5)
                .put("access_token", access_token);
        System.out.println(headers);
    }

    @Test
    public void PostApiTest() {

        // Defining the request body.
        requestBody = """
                        {
                            "application_key": "CFAOONLGDIHBABABA",
                            "description": "new description 1",
                            "format": "json",
                            "method": "photos.editPhoto",
                            "photo_id": "970759870640",
                            "sig": "",
                            "access_token": "-n-eaiE7uRKyhtjqYEFkv58Dds0a0Dqzg4IJUKwv1tT0aADQcl8Oev8EIL5J7UU5Wv5jmIX6aXFFJarrn4Ph"
                        }
                        """;

        //Converting request body to JSON.
        JsonPath expectedResponse = new JsonPath(headers.toString());

        // Performing POST request and validating its response.
        given()
                // Specifying the Base URI.
                .baseUri("https://api.ok.ru/fb.do?")
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                //HTTP Request Method.
                .post()
                .then()
                //Ensuring that status code is 201 OK.
                .statusCode(201)
                //Validating Name value.
                .body("name", equalTo(expectedResponse.getString("name")))
                //Validating Job value.
                .body("job", equalTo(expectedResponse.getString("job")))
                // Ensuring that ID will be generated and returned in response is not null.
                .body("id", notNullValue())
                // Log response details
                .log().all();
    }
}
