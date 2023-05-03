package ru.practicum.shareit.booking.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select b from Booking b where b.booker = :user order by b.start desc")
    List<Booking> findAllByBooker(@Param("user") User user);

    @Query("select b from Booking b where b.booker = :user and b.status = :waiting order by b.start desc")
    List<Booking> findAllByBookerAndStatus(@Param("user") User user, @Param("waiting") Booking.Status waiting);

    @Query("select b from Booking b where b.booker = :user and CURRENT_TIMESTAMP between b.start and b.end order by b.start desc")
    List<Booking> findAllCurrentByBooker(@Param("user") User user);

    @Query("select b from Booking b where b.booker = :user and CURRENT_TIMESTAMP > b.end order by b.start desc")
    List<Booking> findAllPastByBooker(@Param("user") User user);

    @Query("select b from Booking b where b.booker = :user and CURRENT_TIMESTAMP < b.start order by b.start desc")
    List<Booking> findAllFutureByBooker(@Param("user") User user);

    @Query("select b from Booking b where b.item.owner = :user order by b.start desc")
    List<Booking> findAllByOwner(@Param("user") User user);

    @Query("select b from Booking b where b.item.owner = :user and b.status = :waiting order by b.start desc")
    List<Booking> findAllByOwnerAndStatus(@Param("user") User user, @Param("waiting") Booking.Status waiting);

    @Query("select b from Booking b where b.item.owner = :user and CURRENT_TIMESTAMP between b.start and b.end order by b.start desc")
    List<Booking> findAllCurrentByOwner(@Param("user") User user);

    @Query("select b from Booking b where b.item.owner = :user and CURRENT_TIMESTAMP > b.end order by b.start desc")
    List<Booking> findAllPastByOwner(@Param("user") User user);

    @Query("select b from Booking b where b.item.owner = :user and CURRENT_TIMESTAMP < b.start order by b.start desc")
    List<Booking> findAllFutureByOwner(@Param("user") User user);

    List<Booking> findAllByItemAndStatus(Item item, Booking.Status approved);

    @Query("select b from Booking b where b.booker = :user and CURRENT_TIMESTAMP > b.end and b.status = :status order by b.start desc")
    List<Booking> findAllPastByBookerAndStatus(@Param("user") User user, @Param("status") Booking.Status status);
}
