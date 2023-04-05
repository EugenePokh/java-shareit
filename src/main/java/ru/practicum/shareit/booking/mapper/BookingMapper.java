package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

public class BookingMapper {
    public BookingDto toDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setItem(booking.getItem());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setStart(booking.getStart());
        bookingDto.setBooker(booking.getBooker());
        return bookingDto;
    }
}
