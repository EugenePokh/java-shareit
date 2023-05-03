package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingNotFoundException;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingValidationException;
import ru.practicum.shareit.item.service.ItemNotFoundException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {

    public enum State {
        ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED
    }

    private static final String USER_HEADER = "X-Sharer-User-Id";

    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;

    @PostMapping
    public BookingResponseDto post(@Valid @RequestBody BookingRequestDto bookingRequestDto,
                                   @Valid @NotNull @RequestHeader(USER_HEADER) Long userId) {
        Booking booking = BookingMapper.toModel(bookingRequestDto);
        if (bookingRequestDto.getEnd().isBefore(bookingRequestDto.getStart()) || bookingRequestDto.getEnd().isEqual(bookingRequestDto.getStart())) {
            throw new BookingValidationException("Incorrect start and end dates");
        }
        booking.setItem(itemService.findById(bookingRequestDto.getItemId()).orElseThrow(() -> new ItemNotFoundException("No item by id " + bookingRequestDto.getItemId())));
        if (!booking.getItem().getAvailable()) {
            throw new BookingValidationException("Cannot work with unavailable item");
        }
        booking.setBooker(userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId)));

        return BookingMapper.toDto(bookingService.createReservation(booking));

    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto patch(@PathVariable Long bookingId,
                                    @RequestParam Boolean approved,
                                    @Valid @NotNull @RequestHeader(USER_HEADER) Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId));
        Booking booking = bookingService.findById(bookingId).orElseThrow(() -> new BookingNotFoundException("No booking by id " + bookingId));

        return BookingMapper.toDto(bookingService.decideReservation(booking, user, approved));
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto findById(@PathVariable("bookingId") Long id,
                                       @Valid @NotNull @RequestHeader(USER_HEADER) Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId));
        Booking booking = bookingService.findById(id).orElseThrow(() -> new BookingNotFoundException("No booking by id " + id));
        if (user.getId().equals(booking.getBooker().getId()) || user.getId().equals(booking.getItem().getOwner().getId())) {
            return BookingMapper.toDto(booking);
        } else {
            throw new BookingNotFoundException("No such booking for user by id " + id);
        }
    }

    @GetMapping
    public List<BookingResponseDto> findAll(@Valid @NotNull @RequestHeader(USER_HEADER) Long userId,
                                            @RequestParam(defaultValue = "ALL", name = "state") String stateName) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId));

        State state;
        try {
            state = State.valueOf(stateName);
        } catch (Exception e) {
            throw new BookingValidationException("Unknown state: " + stateName);
        }

        List<Booking> bookings;
        switch (state) {
            case ALL:
                bookings = bookingService.findAllByBooker(user);
                break;
            case CURRENT:
                bookings = bookingService.findAllCurrentByBooker(user);
                break;
            case PAST:
                bookings = bookingService.findAllPastByBooker(user);
                break;
            case FUTURE:
                bookings = bookingService.findAllFutureByBooker(user);
                break;
            case WAITING:
                bookings = bookingService.findAllByBookerAndStatus(user, Booking.Status.WAITING);
                break;
            case REJECTED:
                bookings = bookingService.findAllByBookerAndStatus(user, Booking.Status.REJECTED);
                break;
            default:
                throw new BookingValidationException("No solution state: " + state);
        }

        return bookings.stream().map(BookingMapper::toDto).collect(Collectors.toList());

    }

    @GetMapping("/owner")
    public List<BookingResponseDto> findAllByOwner(@Valid @NotNull @RequestHeader(USER_HEADER) Long userId,
                                                   @RequestParam(defaultValue = "ALL", name = "state") String stateName) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId));

        State state;
        try {
            state = State.valueOf(stateName);
        } catch (Exception e) {
            throw new BookingValidationException("Unknown state: " + stateName);
        }

        List<Booking> bookings;
        switch (state) {
            case ALL:
                bookings = bookingService.findAllByOwner(user);
                break;
            case CURRENT:
                bookings = bookingService.findAllCurrentByOwner(user);
                break;
            case PAST:
                bookings = bookingService.findAllPastByOwner(user);
                break;
            case FUTURE:
                bookings = bookingService.findAllFutureByOwner(user);
                break;
            case WAITING:
                bookings = bookingService.findAllByOwnerAndStatus(user, Booking.Status.WAITING);
                break;
            case REJECTED:
                bookings = bookingService.findAllByOwnerAndStatus(user, Booking.Status.REJECTED);
                break;
            default:
                throw new BookingValidationException("No solution state: " + state);
        }

        return bookings.stream().map(BookingMapper::toDto).collect(Collectors.toList());

    }

    @ExceptionHandler(BookingValidationException.class)
    public ResponseEntity<Object> handleException(BookingValidationException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }


}
