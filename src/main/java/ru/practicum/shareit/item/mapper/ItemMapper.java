package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.dto.ItemPostDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemWithBookingResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {

    public static ItemResponseDto toDto(Item item) {
        ItemResponseDto itemDto = new ItemResponseDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setDescription(item.getDescription());

        if (item.getRequest() != null) {
            itemDto.setRequestId(item.getRequest().getId());
        }

        return itemDto;
    }

    public static ItemWithBookingResponseDto toDtoWithBooking(Item item, List<Comment> comments, List<Booking> bookings, User owner) {
        ItemWithBookingResponseDto itemDto = new ItemWithBookingResponseDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setDescription(item.getDescription());

        if (comments != null) {
            itemDto.setComments(comments
                    .stream()
                    .map(CommentMapper::toDto)
                    .collect(Collectors.toList()));
        }

        if (bookings != null && owner.equals(item.getOwner())) {
            bookings.stream()
                    .filter(booking -> booking.getStatus().equals(Booking.Status.APPROVED))
                    .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                    .sorted(Comparator.comparing(Booking::getStart))
                    .findFirst()
                    .ifPresent(booking -> {
                        itemDto.setNextBooking(new ItemWithBookingResponseDto.Booking());
                        itemDto.getNextBooking().setId(booking.getId());
                        itemDto.getNextBooking().setBookerId(booking.getBooker().getId());
                    });

            bookings.stream()
                    .filter(booking -> booking.getStatus().equals(Booking.Status.APPROVED))
                    .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                    .sorted(Comparator.comparing(Booking::getStart).reversed())
                    .findFirst()
                    .ifPresent(booking -> {
                        itemDto.setLastBooking(new ItemWithBookingResponseDto.Booking());
                        itemDto.getLastBooking().setId(booking.getId());
                        itemDto.getLastBooking().setBookerId(booking.getBooker().getId());
                    });
        }

        return itemDto;
    }

    public static Item toModel(ItemPostDto itemDto, User user, ItemRequest itemRequest) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setAvailable(itemDto.getAvailable());

        if (itemRequest != null) {
            item.setRequest(itemRequest);
        }

        item.setDescription(itemDto.getDescription());
        item.setOwner(user);
        return item;
    }

    public static Item toModel(Item item, ItemPatchDto itemDto, User user) {
        if (!item.getOwner().equals(user)) {
            throw new UserNotFoundException("No item owner");
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        return item;
    }
}
