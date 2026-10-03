package online.automationintesting.api;

import io.restassured.response.Response;
import online.automationintesting.models.Booking;
import online.automationintesting.models.BookingAuth;
import online.automationintesting.models.Room;
import online.automationintesting.models.ContactMessage;

import java.util.List;

import static io.restassured.RestAssured.given;

public class BookingClient {

    public String getAuthToken() {
        BookingAuth auth = BookingAuth.builder()
                .username("admin")
                .password("password")
                .build();

        Response response = given()
                .spec(Specs.requestSpec())
                .body(auth)
                .when()
                .post("/auth/login")
                .then()
                .spec(Specs.responseSpec(200))
                .extract().response();

        return response.jsonPath().getString("token");
    }

    public List<Room> getAllRooms() {
        Response response = given()
                .spec(Specs.requestSpec())
                .when()
                .get("/room")
                .then()
                .spec(Specs.responseSpec(200))
                .extract().response();

        return response.jsonPath().getList("rooms", Room.class);
    }

    public Response createBooking(Booking booking, String token) {
        return given()
                .spec(Specs.requestSpecWithToken(token))
                .body(booking)
                .when()
                .post("/booking")
                .then()
                .extract().response();
    }

    public Response createBookingWithToken(Booking booking, String token) {
        return given()
                .spec(Specs.requestSpecWithToken(token))
                .body(booking)
                .when()
                .post("/booking")
                .then()
                .extract().response();
    }

    public Response getBookingById(Integer bookingId, String token) {
        return given()
                .spec(Specs.requestSpecWithToken(token))
                .when()
                .get("/booking/" + bookingId)
                .then()
                .extract().response();
    }

    public Response updateBooking(int bookingId, Booking booking, String token) {
        return given()
                .spec(Specs.requestSpecWithToken(token))
                .body(booking)
                .when()
                .put("/booking/" + bookingId)
                .then()
                .extract().response();
    }

    public Response deleteBooking(int bookingId, String token) {
        return given()
                .spec(Specs.requestSpecWithToken(token))
                .when()
                .delete("/booking/" + bookingId)
                .then()
                .extract().response();
    }

    public Response sendContactMessage(ContactMessage message) {
        return given()
                .spec(Specs.requestSpec())
                .body(message)
                .when()
                .post("/message")
                .then()
                .extract().response();
    }


    public Response getAllMessages(String token) {
        return given()
                .spec(Specs.requestSpecWithToken(token))
                .when()
                .get("/message")
                .then()
                .extract().response();
    }
}