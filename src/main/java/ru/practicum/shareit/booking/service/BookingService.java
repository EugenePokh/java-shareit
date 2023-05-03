package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    Optional<Booking> findById(Long bookingId);

    List<Booking> findAllByBooker(User user);

    List<Booking> findAllByBookerAndStatus(User user, Booking.Status waiting);

    List<Booking> findAllCurrentByBooker(User user);

    List<Booking> findAllPastByBooker(User user);

    List<Booking> findAllFutureByBooker(User user);

    List<Booking> findAllByOwner(User user);

    List<Booking> findAllByOwnerAndStatus(User user, Booking.Status waiting);

    List<Booking> findAllCurrentByOwner(User user);

    List<Booking> findAllPastByOwner(User user);

    List<Booking> findAllFutureByOwner(User user);

    Booking createReservation(Booking booking);

    List<Booking> findAllByItemAndStatus(Item item, Booking.Status approved);

    Booking decideReservation(Booking booking, User user, Boolean approved);

    List<Booking> findAllPastByBookerAndStatus(User user, Booking.Status status);
}
