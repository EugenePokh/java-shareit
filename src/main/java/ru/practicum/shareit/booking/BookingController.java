package ru.practicum.shareit.booking;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingNotFoundException;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private static final String USER_HEADER = "X-Sharer-User-Id";

    private final BookingService bookingService;
    private final BookingMapper bookingMapper;
    private final UserService userService;

    public BookingController(BookingService bookingService, BookingMapper bookingMapper, UserService userService) {
        this.bookingService = bookingService;
        this.bookingMapper = bookingMapper;
        this.userService = userService;
    }

    @PostMapping
    public Booking post(@Valid @RequestBody BookingDto bookingDto,
                        @Valid @NotNull @RequestHeader(USER_HEADER) Long userId) {
        Booking booking = bookingMapper.toModel(bookingDto, userId);
        return bookingService.createReservation(booking);

    }

    @PatchMapping("/{bookingId}")
    public Booking patch(@PathVariable Long bookingId,
                         @RequestParam Boolean approved,
                         @Valid @NotNull @RequestHeader(USER_HEADER) Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId));
        Booking booking = bookingService.findById(bookingId).orElseThrow(() -> new BookingNotFoundException("No booking by id " + bookingId));

        return bookingService.decideReservation(booking, user, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking findById(@PathVariable("bookingId") Long id,
                            @Valid @NotNull @RequestHeader(USER_HEADER) Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId));
        Booking booking = bookingService.findById(id).orElseThrow(() -> new BookingNotFoundException("No booking by id " + id));
        if (user.getId() == booking.getBooker().getId() || user.getId() == booking.getItem().getOwner().getId()) {
            return booking;
        } else {
            throw new BookingNotFoundException("No such booking for user by id " + id);
        }
    }

    @GetMapping
    public List<Booking> findAll(@Valid @NotNull @RequestHeader(USER_HEADER) Long userId,
                                 @RequestParam(defaultValue = "ALL") String state) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId));

        if (state.equals(State.ALL.name())) {
            return bookingService.findAllByBooker(user);
        } else if (state.equals(State.CURRENT.name())) {
            return bookingService.findAllCurrentByBooker(user);
        } else if (state.equals(State.PAST.name())) {
            return bookingService.findAllPastByBooker(user);
        } else if (state.equals(State.FUTURE.name())) {
            return bookingService.findAllFutureByBooker(user);
        } else if (state.equals(State.WAITING.name())) {
            return bookingService.findAllByBookerAndStatus(user, Booking.Status.WAITING);
        } else if (state.equals(State.REJECTED.name())) {
            return bookingService.findAllByBookerAndStatus(user, Booking.Status.REJECTED);
        }

        throw new BookingValidationException("Unknown state: " + state);

    }

    @GetMapping("/owner")
    public List<Booking> findAllByOwner(@Valid @NotNull @RequestHeader(USER_HEADER) Long userId,
                                        @RequestParam(defaultValue = "ALL") String state) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId));

        if (state.equals(State.ALL.name())) {
            return bookingService.findAllByOwner(user);
        } else if (state.equals(State.CURRENT.name())) {
            return bookingService.findAllCurrentByOwner(user);
        } else if (state.equals(State.PAST.name())) {
            return bookingService.findAllPastByOwner(user);
        } else if (state.equals(State.FUTURE.name())) {
            return bookingService.findAllFutureByOwner(user);
        } else if (state.equals(State.WAITING.name())) {
            return bookingService.findAllByOwnerAndStatus(user, Booking.Status.WAITING);
        } else if (state.equals(State.REJECTED.name())) {
            return bookingService.findAllByOwnerAndStatus(user, Booking.Status.REJECTED);
        }

        throw new BookingValidationException("Unknown state: " + state);

    }

    @ExceptionHandler(BookingValidationException.class)
    public ResponseEntity handleException(BookingValidationException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    public enum State {
        ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED
    }
}
