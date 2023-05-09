package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemNotFoundException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestNotFoundException;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private static final String USER_HEADER = "X-Sharer-User-Id";

    private final UserService userService;
    private final BookingService bookingService;
    private final ItemService itemService;
    private final CommentService commentService;
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemResponseDto post(@Valid @RequestBody ItemPostDto itemDto,
                                @Valid @NotNull @RequestHeader(USER_HEADER) Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId));

        ItemRequest itemRequest = null;
        if (itemDto.getRequestId() != null) {
            itemRequest = itemRequestService.findById(itemDto.getRequestId()).orElseThrow(() -> new ItemRequestNotFoundException("No itemRequest by id " + itemDto.getRequestId()));
        }

        Item item = ItemMapper.toModel(itemDto, user, itemRequest);

        return ItemMapper.toDto(itemService.save(item));
    }

    @PostMapping("/{id}/comment")
    public CommentResponseDto postComment(@Valid @RequestBody CommentRequestDto commentRequestDto,
                               @PathVariable Long id,
                               @Valid @NotNull @RequestHeader(USER_HEADER) Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId));
        Item item = itemService.findById(id).orElseThrow(() -> new ItemNotFoundException("No item by id " + id));

        Comment comment = commentService.createCommentForItem(item, user, commentRequestDto.getText());
        return CommentMapper.toDto(comment);
    }

    @PatchMapping("/{id}")
    public ItemResponseDto patch(@PathVariable Long id,
                                 @Valid @RequestBody ItemPatchDto itemDto,
                                 @Valid @NotNull @RequestHeader(USER_HEADER) Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId));
        Item item = itemService.findById(id).orElseThrow(() -> new ItemNotFoundException("No item by id " + id));
        Item patchItem = ItemMapper.toModel(item, itemDto, user);
        return ItemMapper.toDto(itemService.save(patchItem));
    }

    @GetMapping
    public List<ItemWithBookingResponseDto> findAll(@Valid @NotNull @RequestHeader(USER_HEADER) Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId));
        List<ItemWithBookingResponseDto> dtos = itemService.findAllByUser(user)
                .stream()
                .map(ItemMapper::toDtoWithBooking)
                .sorted(Comparator.comparing(ItemWithBookingResponseDto::getId))
                .collect(Collectors.toList());

        dtos.forEach(dto -> normalize(dto, user));

        return dtos;
    }

    @GetMapping("/{id}")
    public ItemWithBookingResponseDto findById(@PathVariable Long id, @Valid @NotNull @RequestHeader(USER_HEADER) Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId));
        ItemWithBookingResponseDto dto = ItemMapper.toDtoWithBooking(itemService.findById(id).orElseThrow(() -> new ItemNotFoundException("No item by id " + id)));
        normalize(dto, user);

        return dto;
    }

    @GetMapping("/search")
    public List<ItemResponseDto> findAllByText(@RequestParam String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemService.findAvailableByText(text).stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    private void normalize(ItemWithBookingResponseDto itemDto, User user) {
        Item item = itemService.findById(itemDto.getId()).get();
        itemDto.setComments(commentService.findAllByItem(item)
                .stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList()));

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
                    .sorted(Comparator.comparing(Booking::getStart).reversed())
                    .findFirst()
                    .ifPresent(booking -> {
                        itemDto.setLastBooking(new ItemWithBookingResponseDto.Booking());
                        itemDto.getLastBooking().setId(booking.getId());
                        itemDto.getLastBooking().setBookerId(booking.getBooker().getId());
                    });
        }
    }

}
