package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingNotFoundException;
import ru.practicum.shareit.item.dto.ItemWithBookingResponseDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestNotFoundException;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/requests")
@AllArgsConstructor
public class ItemRequestController {

    private static final String USER_HEADER = "X-Sharer-User-Id";

    private final UserService userService;
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestResponseDto post(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                       @Valid @NotNull @RequestHeader(USER_HEADER) Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId));
        ItemRequest itemRequest = ItemRequestMapper.toModel(itemRequestDto, user);
        return ItemRequestMapper.toDto(itemRequestService.create(itemRequest));
    }

    @GetMapping
    public List<ItemRequestResponseDto> findAll(@Valid @NotNull @RequestHeader(USER_HEADER) Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId));
        List<ItemRequest> itemRequests = itemRequestService.findAllByRequestor(user);
        return itemRequests.stream()
                .map(ItemRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto findById(@PathVariable("requestId") Long id) {
        ItemRequest itemRequest = itemRequestService.findById(id).orElseThrow(() -> new ItemRequestNotFoundException("No itemRequest by id " + id));
        return ItemRequestMapper.toDto(itemRequest);
    }

    @GetMapping("/all")
    public List<ItemRequestResponseDto> findAllRequestsByOtherUsers(@Valid @NotNull @RequestHeader(USER_HEADER) Long userId,
                                                                    @RequestParam() Integer from,
                                                                    @RequestParam() Integer size) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId));
        PageRequest page = PageRequest.of(page, size, sort);
        List<ItemRequest> itemRequests = itemRequestService.findAllOtherRequestors(user);
        return itemRequests.stream()
                .map(ItemRequestMapper::toDto)
                .collect(Collectors.toList());
    }

}
