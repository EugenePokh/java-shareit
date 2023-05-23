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
import java.util.*;
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
    public ItemResponseDto post(@RequestBody ItemPostDto itemDto,
                                @RequestHeader(USER_HEADER) Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId));

        ItemRequest itemRequest = null;
        if (itemDto.getRequestId() != null) {
            itemRequest = itemRequestService.findById(itemDto.getRequestId()).orElseThrow(() -> new ItemRequestNotFoundException("No itemRequest by id " + itemDto.getRequestId()));
        }

        Item item = ItemMapper.toModel(itemDto, user, itemRequest);

        return ItemMapper.toDto(itemService.save(item));
    }

    @PostMapping("/{id}/comment")
    public CommentResponseDto postComment(@RequestBody CommentRequestDto commentRequestDto,
                                          @PathVariable Long id,
                                          @RequestHeader(USER_HEADER) Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId));
        Item item = itemService.findById(id).orElseThrow(() -> new ItemNotFoundException("No item by id " + id));

        Comment comment = commentService.createCommentForItem(item, user, commentRequestDto.getText());
        return CommentMapper.toDto(comment);
    }

    @PatchMapping("/{id}")
    public ItemResponseDto patch(@PathVariable Long id,
                                 @RequestBody ItemPatchDto itemDto,
                                 @RequestHeader(USER_HEADER) Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId));
        Item item = itemService.findById(id).orElseThrow(() -> new ItemNotFoundException("No item by id " + id));
        Item patchItem = ItemMapper.toModel(item, itemDto, user);
        return ItemMapper.toDto(itemService.save(patchItem));
    }

    @GetMapping
    public List<ItemWithBookingResponseDto> findAll(@RequestHeader(USER_HEADER) Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId));
        List<Item> items = itemService.findAllByUser(user);

        Map<Item, List<Comment>> commentMap = new HashMap<>();
        commentService.findAllByItemIn(items)
                .forEach(comment -> commentMap.merge(comment.getItem(), Arrays.asList(comment), (prev, newOne) -> {
                    List<Comment> res = new ArrayList<>();
                    res.addAll(prev);
                    res.addAll(newOne);

                    return res;
                }));

        Map<Item, List<Booking>> bookingMap = new HashMap<>();
        bookingService.findAllByItemIn(items)
                .forEach(booking -> bookingMap.merge(booking.getItem(), Arrays.asList(booking), (prev, newOne) -> {
                    List<Booking> res = new ArrayList<>();
                    res.addAll(prev);
                    res.addAll(newOne);

                    return res;
                }));

        return items.stream()
                .map(item -> ItemMapper.toDtoWithBooking(item, commentMap.get(item), bookingMap.get(item), user))
                .collect(Collectors.toList());

    }

    @GetMapping("/{id}")
    public ItemWithBookingResponseDto findById(@PathVariable Long id, @RequestHeader(USER_HEADER) Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId));
        Item item = itemService.findById(id).orElseThrow(() -> new ItemNotFoundException("No item by id " + id));
        List<Comment> comments = commentService.findAllByItem(item);
        List<Booking> bookings = bookingService.findAllByItemIn(Arrays.asList(item));

        return ItemMapper.toDtoWithBooking(item, comments, bookings, user);
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

}
