package ru.practicum.shareit.booking.service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoSimple;

import java.util.List;

public interface BookingService {

    BookingDto findById(Long bookingId, Long userId);

    List<BookingDto> findAll(Long userId, String state);

    BookingDto save(BookingDtoSimple bookingDtoSimple, Long userId);

    BookingDto update(Long bookingId, BookingDto bookingDto);

    void deleteById(Long bookingId);

    BookingDto approve(Long userId, Long bookingId, Boolean approved);

    List<BookingDto> findAllByItemOwnerId(Long userId, String state);
}
