package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    Optional<Booking> findById(Long bookingId);

    List<Booking> findAllByBooker(User user, PageRequest page);

    List<Booking> findAllByBookerAndStatus(User user, Booking.Status waiting, PageRequest page);

    List<Booking> findAllCurrentByBooker(User user, PageRequest page);

    List<Booking> findAllPastByBooker(User user, PageRequest page);

    List<Booking> findAllFutureByBooker(User user, PageRequest page);

    List<Booking> findAllByOwner(User user, PageRequest page);

    List<Booking> findAllByOwnerAndStatus(User user, Booking.Status waiting, PageRequest page);

    List<Booking> findAllCurrentByOwner(User user, PageRequest page);

    List<Booking> findAllPastByOwner(User user, PageRequest page);

    List<Booking> findAllFutureByOwner(User user, PageRequest page);

    Booking createReservation(Booking booking);

    List<Booking> findAllByItemAndStatus(Item item, Booking.Status approved);

    Booking decideReservation(Booking booking, User user, Boolean approved);

    List<Booking> findAllPastByBookerAndStatus(User user, Booking.Status status);

    List<Booking> findAllByItemIn(List<Item> items);
}
