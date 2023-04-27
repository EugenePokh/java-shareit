package ru.practicum.shareit.booking.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingValidationException;
import ru.practicum.shareit.item.service.ItemNotFoundException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;



@Component
@AllArgsConstructor
public class BookingMapper {

    private final ItemService itemService;
    private final UserService userService;

    public Booking toModel(BookingDto bookingDto, Long userId) {
        Booking booking = new Booking();
        if (bookingDto.getEnd().isBefore(bookingDto.getStart()) || bookingDto.getEnd().isEqual(bookingDto.getStart())) {
            throw new BookingValidationException("Incorrect start and end dates");
        }
        booking.setItem(itemService.findById(bookingDto.getItemId()).orElseThrow(() -> new ItemNotFoundException("No item by id " + bookingDto.getItemId())));
        if (!booking.getItem().getAvailable()) {
            throw new BookingValidationException("Cannot work with unavailable item");
        }

        booking.setBooker(userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId)));
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        return booking;
    }
}
