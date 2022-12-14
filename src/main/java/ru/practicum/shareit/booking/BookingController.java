package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoSimple;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */

@RestController
@Slf4j
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<BookingDto> findAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.findAll(userId, state);
    }


    @GetMapping("/owner")
    public List<BookingDto> findAllByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.findAllByItemOwnerId(userId, state);
    }

    @PostMapping
    public BookingDto create(@Valid @RequestBody BookingDtoSimple bookingDtoSimple,
                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос к эндпоинту: '{} {}', Бронирование: ItemId: {}", "POST", "/bookings",
                bookingDtoSimple.getItemId());
        return bookingService.save(bookingDtoSimple, userId);
    }


    @PatchMapping("/{bookingId}")
    public BookingDto approve(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long bookingId, @RequestParam Boolean approved) {
        log.info("Получен запрос к эндпоинту: '{} {}', Подтверждение бронирование: ID: {}", "PATCH", "/bookings",
                bookingId);
        return bookingService.approve(userId, bookingId, approved);
    }

    @GetMapping("/{id}")
    public BookingDto findById(@PathVariable Long id, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("GET booking id={}", id);
        return bookingService.findById(id, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        bookingService.deleteById(id);
    }

}
