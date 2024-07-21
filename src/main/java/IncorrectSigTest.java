import org.junit.jupiter.api.BeforeEach;

public class IncorrectSigTest extends BaseTest{

    @BeforeEach
    public void init(){
        headers
                .put("application_key", application_key)
                .put("format", format)
                .put("photo_id", photo_id);

    }
}
