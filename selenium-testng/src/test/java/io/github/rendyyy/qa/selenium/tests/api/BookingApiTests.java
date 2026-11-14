package io.github.rendyyy.qa.selenium.tests.api;

import io.github.rendyyy.qa.common.api.model.Booking;
import io.github.rendyyy.qa.common.api.model.CreatedBooking;
import io.github.rendyyy.qa.selenium.tests.data.BookingFactory;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Map;

import static io.github.rendyyy.qa.selenium.tests.base.TestGroups.REGRESSION;
import static io.github.rendyyy.qa.selenium.tests.base.TestGroups.SMOKE;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class BookingApiTests extends BaseApiTest {

    private static final String INVALID_TOKEN = "invalid-token";

    @Test(groups = {SMOKE, REGRESSION}, description = "Health check endpoint responds")
    public void shouldRespondToHealthCheck() {
        assertThat(bookings().ping().statusCode()).isEqualTo(201);
    }

    @Test(groups = REGRESSION, description = "Booking list is returned with numeric ids")
    public void shouldListBookingIds() {
        Response response = bookings().getBookingIds();

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(200);
            softly.assertThat(response.jsonPath().getList("bookingid", Integer.class))
                    .isNotEmpty()
                    .doesNotContainNull();
        });
    }

    @Test(groups = {SMOKE, REGRESSION}, description = "Creating a booking returns it and persists it")
    public void shouldCreateBooking() {
        Booking payload = BookingFactory.random();

        Response response = bookings().createBooking(payload);

        assertThat(response.statusCode()).isEqualTo(200);
        CreatedBooking created = response.as(CreatedBooking.class);
        track(created.bookingId());

        response.then().body(matchesJsonSchemaInClasspath("schemas/create-booking-response-schema.json"));
        assertSoftly(softly -> {
            softly.assertThat(created.bookingId()).isPositive();
            softly.assertThat(created.booking()).isEqualTo(payload);
            softly.assertThat(bookings().getBooking(created.bookingId()).as(Booking.class)).isEqualTo(payload);
        });
    }

    @Test(groups = REGRESSION, description = "A single booking matches the schema and the created data")
    public void shouldReturnBookingMatchingSchema() {
        Booking payload = BookingFactory.random();
        int id = createBooking(payload);

        Response response = bookings().getBooking(id);

        assertThat(response.statusCode()).isEqualTo(200);
        response.then().body(matchesJsonSchemaInClasspath("schemas/booking-schema.json"));
        assertThat(response.as(Booking.class)).isEqualTo(payload);
    }

    @Test(groups = REGRESSION, description = "A booking can be found by first and last name")
    public void shouldFindBookingByName() {
        Booking booking = BookingFactory.random();
        int id = createBooking(booking);

        Response response = bookings().searchByName(booking.firstname(), booking.lastname());

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(200);
            softly.assertThat(response.jsonPath().getList("bookingid", Integer.class)).contains(id);
        });
    }

    @Test(groups = REGRESSION, description = "PUT replaces the booking when a valid token is used")
    public void shouldUpdateBookingWithValidToken() {
        int id = createBooking(BookingFactory.random());
        Booking replacement = BookingFactory.random();

        Response response = bookings().updateBooking(id, replacement, adminToken());

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(200);
            softly.assertThat(response.as(Booking.class)).isEqualTo(replacement);
            softly.assertThat(bookings().getBooking(id).as(Booking.class)).isEqualTo(replacement);
        });
    }

    @Test(groups = REGRESSION, description = "PATCH changes only the fields that were sent")
    public void shouldPartiallyUpdateBooking() {
        Booking original = BookingFactory.random();
        int id = createBooking(original);

        Response response = bookings().partialUpdate(id, Map.of("firstname", "Patched"), adminToken());

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(200);
            softly.assertThat(response.as(Booking.class)).isEqualTo(original.withFirstname("Patched"));
        });
    }

    @Test(groups = REGRESSION, description = "DELETE removes the booking")
    public void shouldDeleteBooking() {
        int id = createBooking(BookingFactory.random());

        Response deleteResponse = bookings().deleteBooking(id, adminToken());
        Response getResponse = bookings().getBooking(id);

        assertSoftly(softly -> {
            softly.assertThat(deleteResponse.statusCode()).isEqualTo(201);
            softly.assertThat(getResponse.statusCode()).isEqualTo(404);
        });
    }

    @Test(groups = REGRESSION, description = "PUT with an invalid token is forbidden and changes nothing")
    public void shouldRejectUpdateWithInvalidToken() {
        Booking original = BookingFactory.random();
        int id = createBooking(original);

        Response response = bookings().updateBooking(id, BookingFactory.random(), INVALID_TOKEN);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(403);
            softly.assertThat(bookings().getBooking(id).as(Booking.class)).isEqualTo(original);
        });
    }

    @Test(groups = REGRESSION, description = "DELETE with an invalid token is forbidden and keeps the booking")
    public void shouldRejectDeleteWithInvalidToken() {
        int id = createBooking(BookingFactory.random());

        Response response = bookings().deleteBooking(id, INVALID_TOKEN);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(403);
            softly.assertThat(bookings().getBooking(id).statusCode()).isEqualTo(200);
        });
    }

    @Test(groups = REGRESSION, description = "An unknown booking id returns 404")
    public void shouldReturnNotFoundForUnknownBooking() {
        assertThat(bookings().getBooking(Integer.MAX_VALUE).statusCode()).isEqualTo(404);
    }
}