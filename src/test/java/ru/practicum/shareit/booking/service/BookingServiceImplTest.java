package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingServiceImplTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private TestEntityManager entityManager;

    @TestConfiguration
    static class BookingServiceImplTestContextConfiguration {

        @Bean
        public BookingService bookingService(BookingRepository bookingRepository) {
            return new BookingServiceImpl(bookingRepository);
        }
    }

    @Test
    void findAllPastByBookerAndStatus() {
    }

    @Test
    void findById() {
        Booking booking = entityManager.persistAndFlush(Booking.builder()
                .status(Booking.Status.WAITING)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .build());

        Optional<Booking> created = bookingService.findById(booking.getId());
        assertEquals(Optional.of(booking), created);
    }

    @Test
    void findAllByBooker() {
        User user = entityManager.persistAndFlush(User.builder()
                .name("test")
                .email("test@mail.ru")
                .build());

        Booking booking = entityManager.persistAndFlush(Booking.builder()
                .booker(user)
                .status(Booking.Status.WAITING)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .build());

        List<Booking> list = bookingService.findAllByBooker(user, PageRequest.of(0, 5));
        assertEquals(Arrays.asList(booking), list);
    }

    @Test
    void findAllByBookerAndStatus() {
        User user = entityManager.persistAndFlush(User.builder()
                .name("test")
                .email("test@mail.ru")
                .build());

        Booking booking = entityManager.persistAndFlush(Booking.builder()
                .booker(user)
                .status(Booking.Status.APPROVED)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .build());

        List<Booking> list = bookingService.findAllByBookerAndStatus(user, Booking.Status.APPROVED, PageRequest.of(0, 5));
        assertEquals(Arrays.asList(booking), list);
    }

    @Test
    void findAllCurrentByBooker() {
        User user = entityManager.persistAndFlush(User.builder()
                .name("test")
                .email("test@mail.ru")
                .build());

        Booking booking = entityManager.persistAndFlush(Booking.builder()
                .booker(user)
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .status(Booking.Status.WAITING)
                .build());

        List<Booking> list = bookingService.findAllCurrentByBooker(user, PageRequest.of(0, 5));
        assertEquals(Arrays.asList(booking), list);
    }

    @Test
    void findAllPastByBooker() {
        User user = entityManager.persistAndFlush(User.builder()
                .name("test")
                .email("test@mail.ru")
                .build());

        Booking booking = entityManager.persistAndFlush(Booking.builder()
                .booker(user)
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .status(Booking.Status.WAITING)
                .build());

        List<Booking> list = bookingService.findAllPastByBooker(user, PageRequest.of(0, 5));
        assertEquals(Arrays.asList(booking), list);
    }

    @Test
    void findAllFutureByBooker() {
        User user = entityManager.persistAndFlush(User.builder()
                .name("test")
                .email("test@mail.ru")
                .build());

        Booking booking = entityManager.persistAndFlush(Booking.builder()
                .booker(user)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(Booking.Status.WAITING)
                .build());

        List<Booking> list = bookingService.findAllFutureByBooker(user, PageRequest.of(0, 5));
        assertEquals(Arrays.asList(booking), list);
    }

    @Test
    void findAllByOwner() {
        User user = entityManager.persistAndFlush(User.builder()
                .name("test")
                .email("test@mail.ru")
                .build());

        Item item = entityManager.persistAndFlush(Item.builder()
                .owner(user)
                .name("name")
                .description("description")
                .available(true)
                .build());

        Booking booking = entityManager.persistAndFlush(Booking.builder()
                .item(item)
                .status(Booking.Status.WAITING)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .build());


        List<Booking> list = bookingService.findAllByOwner(user, PageRequest.of(0, 5));
        assertEquals(Arrays.asList(booking), list);

    }

    @Test
    void findAllByOwnerAndStatus() {
        User user = entityManager.persistAndFlush(User.builder()
                .name("test")
                .email("test@mail.ru")
                .build());

        Item item = entityManager.persistAndFlush(Item.builder()
                .owner(user)
                .name("name")
                .description("description")
                .available(true)
                .build());

        Booking booking = entityManager.persistAndFlush(Booking.builder()
                .item(item)
                .status(Booking.Status.APPROVED)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .build());

        List<Booking> list = bookingService.findAllByOwnerAndStatus(user, Booking.Status.APPROVED, PageRequest.of(0, 5));
        assertEquals(Arrays.asList(booking), list);
    }

    @Test
    void findAllCurrentByOwner() {
        User user = entityManager.persistAndFlush(User.builder()
                .name("test")
                .email("test@mail.ru")
                .build());

        Item item = entityManager.persistAndFlush(Item.builder()
                .owner(user)
                .name("name")
                .description("description")
                .available(true)
                .build());

        Booking booking = entityManager.persistAndFlush(Booking.builder()
                .item(item)
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(Booking.Status.WAITING)
                .build());

        List<Booking> list = bookingService.findAllCurrentByOwner(user, PageRequest.of(0, 5));
        assertEquals(Arrays.asList(booking), list);
    }

    @Test
    void findAllPastByOwner() {
        User user = entityManager.persistAndFlush(User.builder()
                .name("test")
                .email("test@mail.ru")
                .build());

        Item item = entityManager.persistAndFlush(Item.builder()
                .owner(user)
                .name("name")
                .description("description")
                .available(true)
                .build());

        Booking booking = entityManager.persistAndFlush(Booking.builder()
                .item(item)
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .status(Booking.Status.WAITING)
                .build());

        List<Booking> list = bookingService.findAllPastByOwner(user, PageRequest.of(0, 5));
        assertEquals(Arrays.asList(booking), list);
    }

    @Test
    void findAllFutureByOwner() {
        User user = entityManager.persistAndFlush(User.builder()
                .name("test")
                .email("test@mail.ru")
                .build());

        Item item = entityManager.persistAndFlush(Item.builder()
                .owner(user)
                .name("name")
                .description("description")
                .available(true)
                .build());

        Booking booking = entityManager.persistAndFlush(Booking.builder()
                .item(item)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(Booking.Status.WAITING)
                .build());

        List<Booking> list = bookingService.findAllFutureByOwner(user, PageRequest.of(0, 5));
        assertEquals(Arrays.asList(booking), list);
    }

    @Test
    void createReservation() {
        User user = entityManager.persistAndFlush(User.builder()
                .name("test")
                .email("test@mail.ru")
                .build());

        User user1 = entityManager.persistAndFlush(User.builder()
                .name("test1")
                .email("test1@mail.ru")
                .build());

        Item item = entityManager.persistAndFlush(Item.builder()
                .owner(user)
                .name("name")
                .description("description")
                .available(true)
                .build());

        Booking booking = Booking.builder()
                .item(item)
                .booker(user1)
                .status(Booking.Status.WAITING)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .build();

        Booking created = bookingService.createReservation(booking);
        assertEquals(booking, created);
    }

    @Test
    void createReservationThrowException() {
        User user = entityManager.persistAndFlush(User.builder()
                .name("test")
                .email("test@mail.ru")
                .build());

        Item item = entityManager.persistAndFlush(Item.builder()
                .owner(user)
                .name("name")
                .description("description")
                .available(true)
                .build());

        Booking booking = Booking.builder()
                .item(item)
                .booker(user)
                .status(Booking.Status.WAITING)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .build();

        assertThrows(BookingNotFoundException.class, () -> {
            bookingService.createReservation(booking);
        });
    }

    @Test
    void findAllByItemAndStatus() {
        User user = entityManager.persistAndFlush(User.builder()
                .name("test")
                .email("test@mail.ru")
                .build());

        Item item = entityManager.persistAndFlush(Item.builder()
                .owner(user)
                .name("name")
                .description("description")
                .available(true)
                .build());

        Booking booking = entityManager.persistAndFlush(Booking.builder()
                .item(item)
                .status(Booking.Status.APPROVED)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .build());

        List<Booking> list = bookingService.findAllByItemAndStatus(item, Booking.Status.APPROVED);
        assertEquals(Arrays.asList(booking), list);
    }

    @Test
    void decideReservationApprove() {
        User user = entityManager.persistAndFlush(User.builder()
                .name("test")
                .email("test@mail.ru")
                .build());

        Item item = entityManager.persistAndFlush(Item.builder()
                .owner(user)
                .name("name")
                .description("description")
                .available(true)
                .build());

        Booking booking = entityManager.persistAndFlush(Booking.builder()
                .item(item)
                .status(Booking.Status.WAITING)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .build());

        Booking created = bookingService.decideReservation(booking, user, true);
        assertEquals(Booking.Status.APPROVED, created.getStatus());

    }

    @Test
    void decideReservationReject() {
        User user = entityManager.persistAndFlush(User.builder()
                .name("test")
                .email("test@mail.ru")
                .build());

        Item item = entityManager.persistAndFlush(Item.builder()
                .owner(user)
                .name("name")
                .description("description")
                .available(true)
                .build());

        Booking booking = entityManager.persistAndFlush(Booking.builder()
                .item(item)
                .status(Booking.Status.WAITING)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .build());

        Booking created = bookingService.decideReservation(booking, user, false);
        assertEquals(Booking.Status.REJECTED, created.getStatus());
    }

    @Test
    void decideReservationNotValidStatus() {
        User user = entityManager.persistAndFlush(User.builder()
                .name("test")
                .email("test@mail.ru")
                .build());

        Item item = entityManager.persistAndFlush(Item.builder()
                .owner(user)
                .name("name")
                .description("description")
                .available(true)
                .build());

        Booking booking = entityManager.persistAndFlush(Booking.builder()
                .item(item)
                .status(Booking.Status.APPROVED)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .build());

        assertThrows(BookingValidationException.class, () -> {
            bookingService.decideReservation(booking, user, true);
        });
    }

    @Test
    void decideReservationThrowException() {
        User user = entityManager.persistAndFlush(User.builder()
                .name("test")
                .email("test@mail.ru")
                .build());

        User user1 = entityManager.persistAndFlush(User.builder()
                .name("test1")
                .email("test1@mail.ru")
                .build());

        Item item = entityManager.persistAndFlush(Item.builder()
                .owner(user)
                .name("name")
                .description("description")
                .available(true)
                .build());

        Booking booking = entityManager.persistAndFlush(Booking.builder()
                .item(item)
                .status(Booking.Status.WAITING)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .build());

        assertThrows(BookingNotFoundException.class, () -> {
            bookingService.decideReservation(booking, user1, true);
        });
    }
}