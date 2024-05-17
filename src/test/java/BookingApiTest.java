import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.FileReader;

public class BookingApiTest {

    @Test
    public void testBookingApiAuthentication() throws Exception {
        // Read test data from JSON file
        JsonObject testData = readTestData("src/test/resources/testdata.json");

        // Extracting login data from JSON
        JsonObject authData = testData.getAsJsonObject("auth");
        String username = authData.get("username").getAsString();
        String password = authData.get("password").getAsString();

        // Create an instance of LoginData class with the extracted values
        LoginData loginData = new LoginData(username, password);

        // Build the request with data
        Response response = given()
                .baseUri("https://restful-booker.herokuapp.com")
                .basePath("/auth")
                .contentType("application/json")
                .body(loginData)
                .when()
                .post();

        // Print the response to the console
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        // Validate the response
        response.then()
                .statusCode(200)
                .body("token", is(notNullValue()));
    }

    private JsonObject readTestData(String filePath) throws Exception {
        FileReader reader = new FileReader(filePath);
        return JsonParser.parseReader(reader).getAsJsonObject();
    }

    // Class to represent Login data
    public static class LoginData {
        private String username;
        private String password;

        public LoginData(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}