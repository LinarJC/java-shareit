package ru.practicum.shareit.booking.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdOrderByStartDesc(Long userId);

    List<Booking> searchByItemOwnerId(Long id);

    List<Booking> searchByBookerIdAndItemIdAndEndIsBefore(Long id, Long itemId, LocalDateTime time);

    List<Booking> searchByItemOwnerIdAndStartIsAfterOrderByStartDesc(Long id, LocalDateTime time);

    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime time);

    List<Booking> findByItemIdAndEndIsBeforeOrderByEndDesc(Long id, LocalDateTime time);

    List<Booking> findByItemIdAndStartIsAfterOrderByStartDesc(Long id, LocalDateTime time);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long userId, Status status);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Long id);

    @Query("select b " +
            "from Booking b left join User as us on b.booker.id = us.id " +
            "where us.id = ?1 " +
            "and ?2 between b.start and b.end " +
            "order by b.start DESC")
    List<Booking> findCurrentByBookerIdOrderByStartDesc(Long userId, LocalDateTime time);

    @Query("select b " +
            "from Booking b left join Item as i on b.item.id = i.id " +
            "left join User as us on i.owner.id = us.id " +
            "where us.id = ?1 " +
            "and ?2 between b.start and b.end " +
            "order by b.start DESC")
    List<Booking> findCurrentByItemOwnerIdOrderByStartDesc(Long userId, LocalDateTime time);

    List<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(Long userId, LocalDateTime time);

    List<Booking> findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(Long userId, LocalDateTime time);
}