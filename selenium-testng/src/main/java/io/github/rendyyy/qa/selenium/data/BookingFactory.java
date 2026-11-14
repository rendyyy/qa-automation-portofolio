package io.github.rendyyy.qa.selenium.tests.data;

import io.github.rendyyy.qa.common.api.model.Booking;
import io.github.rendyyy.qa.common.api.model.BookingDates;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/** Builds valid bookings with a unique lastname, so tests never collide on the shared demo server. */
public final class BookingFactory {

    private static final String[] FIRST_NAMES = {"Alya", "Budi", "Citra", "Dimas", "Eka", "Fajar"};
    private static final String[] NEEDS = {"Breakfast", "Late checkout", "Extra pillows", "Airport pickup"};

    private BookingFactory() {
    }

    public static Booking random() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        LocalDate checkin = LocalDate.now().plusDays(random.nextInt(1, 30));
        LocalDate checkout = checkin.plusDays(random.nextInt(1, 15));

        return new Booking(
                FIRST_NAMES[random.nextInt(FIRST_NAMES.length)],
                "Auto" + UUID.randomUUID().toString().substring(0, 8),
                random.nextInt(100, 1000),
                random.nextBoolean(),
                new BookingDates(checkin.toString(), checkout.toString()),
                NEEDS[random.nextInt(NEEDS.length)]);
    }
}