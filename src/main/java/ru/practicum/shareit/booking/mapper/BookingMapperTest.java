package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingMapperTest {

    @Test
    void toModel() {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(5L);
        dto.setStart(LocalDateTime.now());
        dto.setEnd(LocalDateTime.now().plusDays(1));

        Booking booking = BookingMapper.toModel(dto);
        assertNotNull(booking);
    }

    @Test
    void toDto() {
        Booking booking = Booking.builder()
                .id(1L)
                .status(Booking.Status.APPROVED)
                .build();
        BookingResponseDto dto = BookingMapper.toDto(booking);
        assertNotNull(dto);
    }
}