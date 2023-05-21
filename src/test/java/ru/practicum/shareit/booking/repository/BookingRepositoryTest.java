package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    void findAllCurrentByBooker() {
        User user = User.builder()
                .email("test@mail.ru")
                .name("some name")
                .build();
        entityManager.persistAndFlush(user);

        Booking booking1 = Booking.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .booker(user)
                .status(Booking.Status.WAITING)
                .build();

        Booking booking2 = Booking.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .booker(user)
                .status(Booking.Status.WAITING)
                .build();

        Booking booking3 = Booking.builder()
                .start(LocalDateTime.now().minusDays(3))
                .end(LocalDateTime.now().minusDays(1))
                .booker(user)
                .status(Booking.Status.WAITING)
                .build();

        booking1 = entityManager.merge(booking1);
        booking2 = entityManager.merge(booking2);
        booking3 = entityManager.merge(booking3);

        PageRequest pageRequest = PageRequest.of(0, 20);
        List<Booking> bookings = bookingRepository.findAllCurrentByBooker(user, pageRequest);
        assertEquals(1, bookings.size());
        assertEquals(booking1, bookings.get(0));
    }

    @Test
    void findAllPastByBooker() {
        User user = User.builder()
                .email("test@mail.ru")
                .name("some name")
                .build();
        entityManager.persistAndFlush(user);

        Booking booking1 = Booking.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .booker(user)
                .status(Booking.Status.WAITING)
                .build();

        Booking booking2 = Booking.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .booker(user)
                .status(Booking.Status.WAITING)
                .build();

        Booking booking3 = Booking.builder()
                .start(LocalDateTime.now().minusDays(3))
                .end(LocalDateTime.now().minusDays(1))
                .booker(user)
                .status(Booking.Status.WAITING)
                .build();

        booking1 = entityManager.merge(booking1);
        booking2 = entityManager.merge(booking2);
        booking3 = entityManager.merge(booking3);

        PageRequest pageRequest = PageRequest.of(0, 20);
        List<Booking> bookings = bookingRepository.findAllPastByBooker(user, pageRequest);
        assertEquals(1, bookings.size());
        assertEquals(booking3, bookings.get(0));
    }

    @Test
    void findAllFutureByBooker() {
        User user = User.builder()
                .email("test@mail.ru")
                .name("some name")
                .build();
        entityManager.persistAndFlush(user);

        Booking booking1 = Booking.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .booker(user)
                .status(Booking.Status.WAITING)
                .build();

        Booking booking2 = Booking.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .booker(user)
                .status(Booking.Status.WAITING)
                .build();

        Booking booking3 = Booking.builder()
                .start(LocalDateTime.now().minusDays(3))
                .end(LocalDateTime.now().minusDays(1))
                .booker(user)
                .status(Booking.Status.WAITING)
                .build();

        booking1 = entityManager.merge(booking1);
        booking2 = entityManager.merge(booking2);
        booking3 = entityManager.merge(booking3);

        PageRequest pageRequest = PageRequest.of(0, 20);
        List<Booking> bookings = bookingRepository.findAllFutureByBooker(user, pageRequest);
        assertEquals(1, bookings.size());
        assertEquals(booking2, bookings.get(0));
    }

    @Test
    void findAllPastByBookerAndStatus() {
        User user = User.builder()
                .email("test@mail.ru")
                .name("some name")
                .build();
        entityManager.persistAndFlush(user);

        Booking booking1 = Booking.builder()
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .booker(user)
                .status(Booking.Status.APPROVED)
                .build();

        Booking booking2 = Booking.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(Booking.Status.APPROVED)
                .booker(user)
                .build();

        Booking booking3 = Booking.builder()
                .start(LocalDateTime.now().minusDays(3))
                .end(LocalDateTime.now().minusDays(1))
                .booker(user)
                .status(Booking.Status.APPROVED)
                .build();

        booking1 = entityManager.merge(booking1);
        booking2 = entityManager.merge(booking2);
        booking3 = entityManager.merge(booking3);

        List<Booking> bookings = bookingRepository.findAllPastByBookerAndStatus(user, Booking.Status.APPROVED);
        assertEquals(1, bookings.size());
        assertEquals(booking3, bookings.get(0));
    }
}