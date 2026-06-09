package tests;

import base.BaseAPITest;
import endpoints.Routes;
import models.BookingDates;
import models.Bookings;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class  BookingCRUDTest extends BaseAPITest {

    public Logger logger;

    public int bookingId;
    public Bookings bookingPayload;

    @BeforeMethod
    public void createPayload(){
        bookingPayload = new Bookings(
                "Prasadi",
                "Nishshanka",
                400,
                true,
                new BookingDates("2026-05-03","2026-05-05"),
                "Breakfast"
        );
        logger = LogManager.getLogger(this.getClass());
    }

    @Test(priority = 1)
    public void testCreateBooking() {
        logger.info("**** Creates a new booking ****");

        bookingId = given().spec(requestSpecification)
                .body(bookingPayload)
                .when().post(Routes.createBooking())
                .then().spec(responseSpecification).statusCode(200)
                .body("booking.firstname", equalTo("Prasadi"))
                .body("bookingid", notNullValue())
                .extract().path("bookingid");

        System.out.println("Created Booking ID: " + bookingId);
        logger.info("**** New booking is created ****");
    }

    @Test(priority = 2, dependsOnMethods = "testCreateBooking")
    public void testGetBooking() {
        logger.info("**** Reading booking info ****");
        given().spec(requestSpecification)
                .when().get(Routes.getBooking(bookingId))
                .then().spec(responseSpecification).statusCode(200)
                .body("firstname", equalTo(bookingPayload.getFirstname()))
                .body(matchesJsonSchemaInClasspath("booking-schema.json"));
        logger.info("**** Booking info is displayed ****");
    }

    @Test(priority = 3, dependsOnMethods = "testCreateBooking")
    public void testUpdateBooking() {
        logger.info("**** Updates the created booking ****");
        bookingPayload.setFirstname("Prasa");
        bookingPayload.setTotalprice(500);

        given().spec(requestSpecification)
                .body(bookingPayload)
                .when().put(Routes.updateBooking(bookingId))
                .then().spec(responseSpecification).statusCode(200)
                .body("firstname", equalTo("Prasa"))
                .body("totalprice", equalTo(500));
        logger.info("**** Booking info is updated ****");
    }

    @Test(priority = 4, dependsOnMethods = "testCreateBooking")
    public void testDeleteBooking() {
        logger.info("**** Deletes the booking ****");
        given().spec(requestSpecification)
                .when().delete(Routes.deleteBooking(bookingId))
                .then().statusCode(201);

        // Verify deletion
        given().spec(requestSpecification)
                .when().get(Routes.getBooking(bookingId))
                .then().statusCode(404);
        logger.info("**** Booking is deleted ****");
    }

    @Test(priority = 5)
    public void testCreateBooking_InvalidData() {
        Bookings invalid = new Bookings(null, "", -100, true, null, "");

        given().spec(requestSpecification)
                .body(invalid)
                .when().post(Routes.createBooking())
                .then().statusCode(500);
    }

}
