package ru.practicum.shareit.booking.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select b from Booking b where b.booker = ?1 order by b.start desc")
    List<Booking> findAllByBooker(User user);

    @Query("select b from Booking b where b.booker = ?1 and b.status = ?2 order by b.start desc")
    List<Booking> findAllByBookerAndStatus(User user, Booking.Status waiting);

    @Query("select b from Booking b where b.booker = ?1 and CURRENT_DATE between b.start and b.end order by b.start desc")
    List<Booking> findAllCurrentByBooker(User user);

    @Query("select b from Booking b where b.booker = ?1 and CURRENT_DATE > b.end order by b.start desc")
    List<Booking> findAllPastByBooker(User user);

    @Query("select b from Booking b where b.booker = ?1 and CURRENT_DATE < b.start order by b.start desc")
    List<Booking> findAllFutureByBooker(User user);

    @Query("select b from Booking b where b.item.owner = ?1 order by b.start desc")
    List<Booking> findAllByOwner(User user);

    @Query("select b from Booking b where b.item.owner = ?1 and b.status = ?2 order by b.start desc")
    List<Booking> findAllByOwnerAndStatus(User user, Booking.Status waiting);

    @Query("select b from Booking b where b.item.owner = ?1 and CURRENT_DATE between b.start and b.end order by b.start desc")
    List<Booking> findAllCurrentByOwner(User user);

    @Query("select b from Booking b where b.item.owner = ?1 and CURRENT_DATE > b.end order by b.start desc")
    List<Booking> findAllPastByOwner(User user);

    @Query("select b from Booking b where b.item.owner = ?1 and CURRENT_DATE < b.start order by b.start desc")
    List<Booking> findAllFutureByOwner(User user);

    List<Booking> findAllByItemAndStatus(Item item, Booking.Status approved);
}
