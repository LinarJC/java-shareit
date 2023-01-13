package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerId(long userId, Pageable pageable);

    List<Booking> searchByItemOwnerId(Long id, Pageable pageable);

    List<Booking> searchByBookerIdAndItemIdAndEndIsBeforeAndStatus(long id, long itemId,
                                                                   LocalDateTime time, Status status);

    List<Booking> searchByItemOwnerIdAndStartIsAfter(long id,
                                                            LocalDateTime time,
                                                            Pageable pageable);

    List<Booking> findByBookerIdAndStartAfter(long userId, LocalDateTime time,
                                              Pageable pageable);

    List<Booking> findByItemIdAndEndIsBeforeOrderByEndDesc(long id, LocalDateTime time);

    List<Booking> findByItemIdAndStartIsAfterOrderByStartDesc(long id, LocalDateTime time);

    List<Booking> findByBookerIdAndStatus(long userId, Status status,
                                                  Pageable pageable);

    List<Booking> findByItemOwnerId(long id, Pageable pageable);

    @Query("select b " +
            "from Booking b left join User as us on b.booker.id = us.id " +
            "where us.id = ?1 " +
            "and ?2 between b.start and b.end " +
            "order by b.start DESC")
    List<Booking> findCurrentByBookerId(long userId, LocalDateTime time,
                                                Pageable pageable);

    @Query("select b " +
            "from Booking b left join Item as i on b.item.id = i.id " +
            "left join User as us on i.owner.id = us.id " +
            "where us.id = ?1 " +
            "and ?2 between b.start and b.end " +
            "order by b.start DESC")
    List<Booking> findCurrentByItemOwnerId(long userId, LocalDateTime time,
                                                   Pageable pageable);

    List<Booking> findByBookerIdAndEndIsBefore(long userId, LocalDateTime time,
                                                       Pageable pageable);

    List<Booking> findByItemOwnerIdAndEndIsBefore(long userId,
                                                          LocalDateTime time,
                                                          Pageable pageable);
}