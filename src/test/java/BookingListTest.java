import io.restassured.response.Response;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class BookingListTest {

    @Test
    public void testBookingListNotEmpty() {
        // Authenticate and get the token
        String token = authenticate();

        // Request to retrieve list of bookings
        Response response = given()
                .baseUri("https://restful-booker.herokuapp.com")
                .basePath("/booking")
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .when()
                .get();

        // Print the response to the console
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        // Validate the response
        response.then()
                .statusCode(200)
                .body("size()", greaterThan(0)); // Asserting that list size is greater than zero
    }

    // Authenticate and get the token
    private String authenticate() {
        Response response = given()
                .baseUri("https://restful-booker.herokuapp.com")
                .basePath("/auth")
                .contentType("application/json")
                .body("{\"username\":\"admin\",\"password\":\"password123\"}")
                .when()
                .post();

        return response.then().extract().path("token");
    }
}