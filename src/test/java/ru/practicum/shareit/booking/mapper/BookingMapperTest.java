package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

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
        assertEquals(dto.getStart(), booking.getStart());
        assertEquals(dto.getEnd(), booking.getEnd());
    }

    @Test
    void toDto() {
        Booking booking = Booking.builder()
                .id(1L)
                .status(Booking.Status.APPROVED)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .booker(User.builder()
                        .id(1L)
                        .name("name")
                        .email("email@email.ru")
                        .build())
                .item(Item.builder()
                        .id(1L)
                        .name("name")
                        .description("desc")
                        .build())
                .build();

        BookingResponseDto dto = BookingMapper.toDto(booking);

        assertEquals(booking.getId(), dto.getId());
        assertEquals(booking.getStatus(), dto.getStatus());
        assertEquals(booking.getStart(), dto.getStart());
        assertEquals(booking.getEnd(), dto.getEnd());
    }
}