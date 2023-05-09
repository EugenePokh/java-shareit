package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
    public List<Booking> findAllPastByBookerAndStatus(User user, Booking.Status status) {
        return bookingRepository.findAllPastByBookerAndStatus(user, status);
    }

    @Override
    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public List<Booking> findAllByBooker(User user, PageRequest page) {
        return bookingRepository.findAllByBooker(user, page);
    }

    @Override
    public List<Booking> findAllByBookerAndStatus(User user, Booking.Status waiting, PageRequest page) {
        return bookingRepository.findAllByBookerAndStatus(user, waiting, page);
    }

    @Override
    public List<Booking> findAllCurrentByBooker(User user, PageRequest page) {
        return bookingRepository.findAllCurrentByBooker(user, page);
    }

    @Override
    public List<Booking> findAllPastByBooker(User user, PageRequest page) {
        return bookingRepository.findAllPastByBooker(user, page);
    }

    @Override
    public List<Booking> findAllFutureByBooker(User user, PageRequest page) {
        return bookingRepository.findAllFutureByBooker(user, page);
    }

    @Override
    public List<Booking> findAllByOwner(User user, PageRequest page) {
        return bookingRepository.findAllByOwner(user, page);
    }

    @Override
    public List<Booking> findAllByOwnerAndStatus(User user, Booking.Status waiting, PageRequest page) {
        return bookingRepository.findAllByOwnerAndStatus(user, waiting, page);
    }

    @Override
    public List<Booking> findAllCurrentByOwner(User user, PageRequest page) {
        return bookingRepository.findAllCurrentByOwner(user, page);
    }

    @Override
    public List<Booking> findAllPastByOwner(User user, PageRequest page) {
        return bookingRepository.findAllPastByOwner(user);
    }

    @Override
    public List<Booking> findAllFutureByOwner(User user, PageRequest page) {
        return bookingRepository.findAllFutureByOwner(user, page);
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
