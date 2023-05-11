package ru.practicum.shareit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.booking.service.BookingNotFoundException;
import ru.practicum.shareit.booking.service.BookingValidationException;
import ru.practicum.shareit.item.service.ItemNotFoundException;
import ru.practicum.shareit.request.service.ItemRequestNotFoundException;
import ru.practicum.shareit.user.service.UserNotFoundException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(BookingValidationException.class)
    public ResponseEntity<Object> handleBookingValidationException(BookingValidationException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler({BookingNotFoundException.class, ItemNotFoundException.class,
            ItemRequestNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(RuntimeException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }
}
