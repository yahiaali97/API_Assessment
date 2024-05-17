import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.FileReader;

public class BookingCreationTest {

    @Test
    public void testBookingCreation() throws Exception {
        // Read test data from JSON file
        JsonObject testData = readTestData("src/test/resources/testdata.json");

        // Extracting booking data from JSON
        JsonObject bookingData = testData.getAsJsonObject("booking");

        // Authenticate and get the token
        String token = authenticate(testData.getAsJsonObject("auth"));

        // Build the request to create a booking with the obtained token
        Response response = given()
                .baseUri("https://restful-booker.herokuapp.com")
                .basePath("/booking")
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(bookingData)
                .when()
                .post();

        // Print the response to the console
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        // Validate the response
        response.then()
                .statusCode(200)
                .body("bookingid", is(notNullValue()));
    }

    // Authenticate and get the token
    private String authenticate(JsonObject authData) {
        Response response = given()
                .baseUri("https://restful-booker.herokuapp.com")
                .basePath("/auth")
                .contentType("application/json")
                .body(authData.toString())
                .when()
                .post();

        return response.then().extract().path("token");
    }

    private JsonObject readTestData(String filePath) throws Exception {
        FileReader reader = new FileReader(filePath);
        return JsonParser.parseReader(reader).getAsJsonObject();
    }
}