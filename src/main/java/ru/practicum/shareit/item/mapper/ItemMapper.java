package ru.practicum.shareit.item.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemWithBookingResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Component
public class ItemMapper {
    private final BookingService bookingService;

    public ItemResponseDto toDto(Item item) {
        ItemResponseDto itemDto = new ItemResponseDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setDescription(item.getDescription());
        return itemDto;
    }

    public ItemWithBookingResponseDto toDtoWithBooking(Item item, User user) {
        ItemWithBookingResponseDto itemDto = new ItemWithBookingResponseDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setDescription(item.getDescription());

        if (user.equals(item.getOwner())) {

            List<Booking> bookings = bookingService.findAllByItemAndStatus(item, Booking.Status.APPROVED);
            bookings.stream()
                    .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                    .sorted(Comparator.comparing(Booking::getStart))
                    .findFirst()
                    .ifPresent(booking -> {
                        itemDto.setNextBooking(new ItemWithBookingResponseDto.Booking());
                        itemDto.getNextBooking().setId(booking.getId());
                        itemDto.getNextBooking().setBookerId(booking.getBooker().getId());
                    });

            bookings.stream()
                    .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                    .sorted(Comparator.comparing(Booking::getStart))
                    .findFirst()
                    .ifPresent(booking -> {
                        itemDto.setLastBooking(new ItemWithBookingResponseDto.Booking());
                        itemDto.getLastBooking().setId(booking.getId());
                        itemDto.getLastBooking().setBookerId(booking.getBooker().getId());
                    });
        }


        return itemDto;
    }
}
