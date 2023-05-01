package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    @Override
    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public List<Booking> findAllByBooker(User user) {
        return bookingRepository.findAllByBooker(user);
    }

    @Override
    public List<Booking> findAllByBookerAndStatus(User user, Booking.Status waiting) {
        return bookingRepository.findAllByBookerAndStatus(user, waiting);
    }

    @Override
    public List<Booking> findAllCurrentByBooker(User user) {
        return bookingRepository.findAllCurrentByBooker(user);
    }

    @Override
    public List<Booking> findAllPastByBooker(User user) {
        return bookingRepository.findAllPastByBooker(user);
    }

    @Override
    public List<Booking> findAllFutureByBooker(User user) {
        return bookingRepository.findAllFutureByBooker(user);
    }

    @Override
    public List<Booking> findAllByOwner(User user) {
        return bookingRepository.findAllByOwner(user);
    }

    @Override
    public List<Booking> findAllByOwnerAndStatus(User user, Booking.Status waiting) {
        return bookingRepository.findAllByOwnerAndStatus(user, waiting);
    }

    @Override
    public List<Booking> findAllCurrentByOwner(User user) {
        return bookingRepository.findAllCurrentByOwner(user);
    }

    @Override
    public List<Booking> findAllPastByOwner(User user) {
        return bookingRepository.findAllPastByOwner(user);
    }

    @Override
    public List<Booking> findAllFutureByOwner(User user) {
        return bookingRepository.findAllFutureByOwner(user);
    }

    @Override
    public Booking createReservation(Booking booking) {
        if (booking.getBooker().getId().equals(booking.getItem().getOwner().getId())) {
            throw new BookingNotFoundException("Owner can't book his items");
        }

        booking.setStatus(Booking.Status.WAITING);
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> findAllByItemAndStatus(Item item, Booking.Status approved) {
        return bookingRepository.findAllByItemAndStatus(item, approved);
    }

    @Override
    public Booking decideReservation(Booking booking, User user, Boolean approved) {
        if (!booking.getItem().getOwner().equals(user)) {
            throw new BookingNotFoundException("No such booking for user by id " + user.getId());
        }

        if (!booking.getStatus().equals(Booking.Status.WAITING)) {
            throw new BookingValidationException("Status already define");
        }

        if (approved) {
            booking.setStatus(Booking.Status.APPROVED);
        } else {
            booking.setStatus(Booking.Status.REJECTED);
        }

        return bookingRepository.save(booking);
    }
}
