package ru.practicum.shareit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.booking.service.BookingNotFoundException;
import ru.practicum.shareit.booking.service.BookingValidationException;
import ru.practicum.shareit.item.service.ItemNotFoundException;
import ru.practicum.shareit.request.service.ItemRequestNotFoundException;
import ru.practicum.shareit.user.service.UserNotFoundException;
import ru.practicum.shareit.user.service.UserValidationException;

import java.util.Map;

@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(BookingValidationException.class)
    public ResponseEntity<Object> handleBookingValidationException(BookingValidationException ex) {
        Map<String, String> errors = Map.of("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler({BookingNotFoundException.class, ItemNotFoundException.class,
            ItemRequestNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(RuntimeException ex) {
        Map<String, String> errors = Map.of("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler({UserValidationException.class})
    public ResponseEntity<Object> handleUserValidationException(RuntimeException ex) {
        Map<String, String> errors = Map.of("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = Map.of("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleUnexpectedException(Throwable ex) {
        Map<String, String> errors = Map.of("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
    }
}
