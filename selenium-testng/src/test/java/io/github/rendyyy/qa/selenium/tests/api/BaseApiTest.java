package io.github.rendyyy.qa.selenium.tests.api;

import io.github.rendyyy.qa.common.api.AuthClient;
import io.github.rendyyy.qa.common.api.BookingClient;
import io.github.rendyyy.qa.common.api.model.Booking;
import io.github.rendyyy.qa.common.api.model.CreatedBooking;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/** Shared helpers for API tests. Deliberately does not extend BaseTest, so no browser is started. */
public abstract class BaseApiTest {

    private static final Logger LOG = LogManager.getLogger(BaseApiTest.class);

    // One list per thread: tests run in parallel on the same instance, @AfterMethod runs on the same thread.
    private final ThreadLocal<List<Integer>> createdBookingIds = ThreadLocal.withInitial(ArrayList::new);

    protected BookingClient bookings() {
        return new BookingClient();
    }

    protected String adminToken() {
        return new AuthClient().fetchAdminToken();
    }

    /** Creates a booking as test fixture and registers it for cleanup. */
    protected int createBooking(Booking booking) {
        Response response = bookings().createBooking(booking);
        assertThat(response.statusCode()).as("fixture: create booking").isEqualTo(200);
        int id = response.as(CreatedBooking.class).bookingId();
        track(id);
        return id;
    }

    protected void track(int bookingId) {
        createdBookingIds.get().add(bookingId);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUpBookings() {
        List<Integer> ids = createdBookingIds.get();
        try {
            if (!ids.isEmpty()) {
                String token = adminToken();
                ids.forEach(id -> bookings().deleteBooking(id, token));
            }
        } catch (RuntimeException e) {
            // The demo server resets itself every few minutes, so a failed cleanup must not fail the test.
            LOG.warn("Cleanup of bookings {} failed: {}", ids, e.getMessage());
        } finally {
            createdBookingIds.remove();
        }
    }
}