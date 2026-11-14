package io.github.rendyyy.qa.common.api;

import io.github.rendyyy.qa.common.api.model.Booking;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

/** Thin wrapper over the booking endpoints. Returns raw responses so tests decide what to assert. */
public class BookingClient {

    private static final String PING_PATH = "/ping";
    private static final String BOOKING_PATH = "/booking";
    private static final String BOOKING_BY_ID_PATH = "/booking/{id}";
    private static final String TOKEN_COOKIE = "token";

    private final RequestSpecification spec;

    public BookingClient() {
        this.spec = ApiSpecs.base();
    }

    @Step("Health check")
    public Response ping() {
        return given().spec(spec).get(PING_PATH);
    }

    @Step("Get all booking ids")
    public Response getBookingIds() {
        return given().spec(spec).get(BOOKING_PATH);
    }

    @Step("Search bookings by name {firstname} {lastname}")
    public Response searchByName(String firstname, String lastname) {
        return given().spec(spec)
                .queryParam("firstname", firstname)
                .queryParam("lastname", lastname)
                .get(BOOKING_PATH);
    }

    @Step("Get booking {id}")
    public Response getBooking(int id) {
        return given().spec(spec).pathParam("id", id).get(BOOKING_BY_ID_PATH);
    }

    @Step("Create a booking")
    public Response createBooking(Booking booking) {
        return given().spec(spec).body(booking).post(BOOKING_PATH);
    }

    @Step("Update booking {id}")
    public Response updateBooking(int id, Booking booking, String token) {
        return given().spec(spec)
                .cookie(TOKEN_COOKIE, token)
                .pathParam("id", id)
                .body(booking)
                .put(BOOKING_BY_ID_PATH);
    }

    @Step("Partially update booking {id}")
    public Response partialUpdate(int id, Map<String, Object> fields, String token) {
        return given().spec(spec)
                .cookie(TOKEN_COOKIE, token)
                .pathParam("id", id)
                .body(fields)
                .patch(BOOKING_BY_ID_PATH);
    }

    @Step("Delete booking {id}")
    public Response deleteBooking(int id, String token) {
        return given().spec(spec)
                .cookie(TOKEN_COOKIE, token)
                .pathParam("id", id)
                .delete(BOOKING_BY_ID_PATH);
    }
}