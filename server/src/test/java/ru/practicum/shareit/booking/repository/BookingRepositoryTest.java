package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;

    private Item item;
    private User owner;
    private User booker;
    private Booking booking;

    @BeforeEach
    void beforeEach() {
        owner = userRepository.save(new User(1L, "user1", "user1@mail.ru"));
        booker = userRepository.save(new User(2L, "user2", "user2@mail.ru"));
        item = itemRepository.save(new Item(1L, "item", "description",
                true, owner, null));
        booking = bookingRepository.save(new Booking(1L, LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(2),
                item, booker, Status.APPROVED));
    }

    @AfterEach
    void afterEach() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findByBookerIdTest() {
        final List<Booking> bookings = bookingRepository.findByBookerId(booker.getId(),
                Pageable.unpaged());
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertSame(booking, bookings.get(0));
    }

    @Test
    void findByBookerIdAndLocalDateTimeTest() {
        List<Booking> bookings = bookingRepository
                .findByBookerIdAndStartAfter(booker.getId(),
                        LocalDateTime.now(),
                        Pageable.unpaged());
        assertNotNull(bookings);
        assertEquals(0, bookings.size());
    }

    @Test
    void searchBookingByItemOwnerIdTest() {
        final List<Booking> bookings = bookingRepository.searchByItemOwnerId(owner.getId(),
                Pageable.unpaged());
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertSame(booking, bookings.get(0));
    }

    @Test
    void searchBookingByBookerIdAndItemIdAndEndIsBeforeAndStatusTest() {
        final List<Booking> bookings = bookingRepository
                .searchByBookerIdAndItemIdAndEndIsBeforeAndStatus(booker.getId(),
                        item.getId(),
                        LocalDateTime.now(),
                        Status.APPROVED);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertSame(booking, bookings.get(0));
    }

    @Test
    void startBookingIsAfterTest() {
        List<Booking> bookings;
        final Booking booking1 = bookingRepository.save(new Booking(2L, LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(10),
                item, booker, Status.APPROVED));
        bookings = bookingRepository
                .searchByItemOwnerIdAndStartIsAfter(owner.getId(),
                        LocalDateTime.now(),
                        Pageable.unpaged());
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertSame(booking1, bookings.get(0));
    }

    @Test
    void findBookingsByItemIdAndEndIsBeforeOrderByEndDescTest() {
        final List<Booking> bookings = bookingRepository
                .findByItemIdAndEndIsBeforeOrderByEndDesc(item.getId(),
                        LocalDateTime.now());
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertSame(booking, bookings.get(0));
    }

    @Test
    void findBookingsByItemIdTest() {
        List<Booking> bookings = bookingRepository
                .findByItemIdAndStartIsAfterOrderByStartDesc(item.getId(),
                        LocalDateTime.now());
        assertNotNull(bookings);
        assertEquals(0, bookings.size());
    }

    @Test
    void startIsAfterOrderByStartDescTest() {
        List<Booking> bookings;
        final Booking booking1 = bookingRepository.save(new Booking(2L,
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(10),
                item, booker, Status.APPROVED));
        bookings = bookingRepository
                .findByItemIdAndStartIsAfterOrderByStartDesc(item.getId(),
                        LocalDateTime.now());
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertSame(booking1, bookings.get(0));
    }

    @Test
    void findBookingsByBookerIdAndStatusTest() {
        final List<Booking> bookings = bookingRepository
                .findByBookerIdAndStatus(booker.getId(),
                        Status.APPROVED,
                        Pageable.unpaged());
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertSame(booking, bookings.get(0));
    }

    @Test
    void findBookingsByItemOwnerIdTest() {
        final List<Booking> bookings = bookingRepository
                .findByItemOwnerId(item.getOwner().getId(),
                        Pageable.unpaged());
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertSame(booking, bookings.get(0));
    }

    @Test
    void findCurrentBookingsByBookerIdTest() {
        List<Booking> bookings;
        final Booking booking1 = bookingRepository.save(new Booking(2L,
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().plusDays(10),
                item, booker, Status.APPROVED));
        bookings = bookingRepository
                .findCurrentByBookerId(booker.getId(),
                        LocalDateTime.now(),
                        Pageable.unpaged());
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertSame(booking1, bookings.get(0));
    }

    @Test
    void findCurrentBookingsByItemOwnerIdTest() {
        List<Booking> bookings;
        final Booking booking1 = bookingRepository.save(new Booking(2L,
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().plusDays(10),
                item, booker, Status.APPROVED));
        bookings = bookingRepository
                .findCurrentByItemOwnerId(item.getOwner().getId(),
                        LocalDateTime.now(),
                        Pageable.unpaged());
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertSame(booking1, bookings.get(0));
    }

    @Test
    void findBookingsByBookerIdAndEndIsBeforeTest() {
        final List<Booking> bookings = bookingRepository
                .findByBookerIdAndEndIsBefore(booker.getId(),
                        LocalDateTime.now(),
                        Pageable.unpaged());
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertSame(booking, bookings.get(0));
    }

    @Test
    void findBookingsByItemOwnerIdAndEndIsBeforeTest() {
        final List<Booking> bookings = bookingRepository
                .findByItemOwnerIdAndEndIsBefore(item.getOwner().getId(),
                        LocalDateTime.now(),
                        Pageable.unpaged());
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertSame(booking, bookings.get(0));
    }
}