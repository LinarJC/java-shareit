package ru.practicum.shareit.booking.bookingTestUtils;

import ru.practicum.shareit.booking.dto.BookingDtoSimple;
import ru.practicum.shareit.booking.model.Booking;

public class TestUtils {
    public static BookingDtoSimple toBookingDtoSimple(Booking booking) {
        return new BookingDtoSimple(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId());
    }
}
