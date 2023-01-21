package ru.practicum.shareit.booking.service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoSimple;

import java.util.List;

public interface BookingService {

    BookingDto findById(Long bookingId, long userId);

    List<BookingDto> findAll(Long userId, String state, int from, int size);


    BookingDto save(BookingDtoSimple bookingDtoSimple, long userId);

    BookingDto update(Long bookingId, BookingDto bookingDto);

    void deleteById(Long bookingId);

    BookingDto approve(long userId, Long bookingId, Boolean approved);

    List<BookingDto> findAllByItemOwnerId(Long userId, String state, int from, int size);
}
