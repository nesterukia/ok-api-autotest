import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;

public abstract class BaseTest {
    public JSONObject headers = new JSONObject();
    public final String application_key = "CFAOONLGDIHBABABA";
    public final String application_secret_key = "C5601461D042CBF949FA3548";
    public final String access_token = "-n-eaiE7uRKyhtjqYEFkv58Dds0a0Dqzg4IJUKwv1tT0aADQcl8Oev8EIL5J7UU5Wv5jmIX6aXFFJarrn4Ph";
    public final String format = "json";
    public final String BASE_URI = "https://api.ok.ru/";
    public String photo_id = "970759870640";
    public String requestBody="";
    public String sig="";

    public void generateRequestBody(){
        ArrayList<String> keys = new ArrayList<>(headers.keySet());
        Collections.sort(keys);
        keys.forEach(keyStr ->{
            Object keyValue = headers.get(keyStr);
            if(!keyStr.equals("method")) {
                requestBody = requestBody.concat("&" + keyStr + "=" + keyValue);
            }
        });
    }
    public String generateSIG(JSONObject headers, String access_token, String application_secret_key) throws NoSuchAlgorithmException {
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
        return new String(Hex.encodeHex(sigBytes)).toLowerCase();
    }

    @BeforeEach
    public void init() {
        headers
                .put("application_key", application_key)
                .put("format", format)
                .put("photo_id", photo_id);
        try{
            sig = generateSIG(headers, access_token, application_secret_key);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        headers
                .put("sig", sig)
                .put("access_token", access_token);
        generateRequestBody();
    }
}
