package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBooker(User user, PageRequest pageRequest);

    List<Booking> findAllByBookerAndStatus(User user, Booking.Status waiting, PageRequest page);

    @Query("select b from Booking b where b.booker = :user and CURRENT_TIMESTAMP between b.start and b.end")
    List<Booking> findAllCurrentByBooker(@Param("user") User user, PageRequest page);

    @Query("select b from Booking b where b.booker = :user and CURRENT_TIMESTAMP > b.end")
    List<Booking> findAllPastByBooker(@Param("user") User user, PageRequest page);

    @Query("select b from Booking b where b.booker = :user and CURRENT_TIMESTAMP < b.start")
    List<Booking> findAllFutureByBooker(@Param("user") User user, PageRequest page);

    @Query("select b from Booking b where b.item.owner = :user")
    List<Booking> findAllByOwner(@Param("user") User user, PageRequest page);

    @Query("select b from Booking b where b.item.owner = :user and b.status = :waiting")
    List<Booking> findAllByOwnerAndStatus(@Param("user") User user, @Param("waiting") Booking.Status waiting, PageRequest page);

    @Query("select b from Booking b where b.item.owner = :user and CURRENT_TIMESTAMP between b.start and b.end")
    List<Booking> findAllCurrentByOwner(@Param("user") User user, PageRequest page);

    @Query("select b from Booking b where b.item.owner = :user and CURRENT_TIMESTAMP > b.end order by b.start desc")
    List<Booking> findAllPastByOwner(@Param("user") User user);

    @Query("select b from Booking b where b.item.owner = :user and CURRENT_TIMESTAMP < b.start")
    List<Booking> findAllFutureByOwner(@Param("user") User user, PageRequest page);

    List<Booking> findAllByItemAndStatus(Item item, Booking.Status approved);

    @Query("select b from Booking b where b.booker = :user and CURRENT_TIMESTAMP > b.end and b.status = :status order by b.start desc")
    List<Booking> findAllPastByBookerAndStatus(@Param("user") User user, @Param("status") Booking.Status status);

    List<Booking> findAllByItemIn(List<Item> items);
}
