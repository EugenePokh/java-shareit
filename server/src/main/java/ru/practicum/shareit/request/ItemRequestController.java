package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestNotFoundException;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/requests")
@AllArgsConstructor
@Validated
public class ItemRequestController {

    private static final String USER_HEADER = "X-Sharer-User-Id";

    private final UserService userService;
    private final ItemRequestService itemRequestService;
    private final ItemService itemService;

    @PostMapping
    public ItemRequestResponseDto post(@RequestBody ItemRequestDto itemRequestDto,
                                       @RequestHeader(USER_HEADER) Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId));
        ItemRequest itemRequest = ItemRequestMapper.toModel(itemRequestDto, user);
        return ItemRequestMapper.toDto(itemRequestService.create(itemRequest), null);
    }

    @GetMapping
    public List<ItemRequestResponseDto> findAll(@RequestHeader(USER_HEADER) Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId));
        List<ItemRequest> requests = itemRequestService.findAllByRequestor(user, Sort.by("created").descending());
        Map<ItemRequest, List<Item>> listMap = new HashMap<>();
        itemService.findAllByRequests(requests)
                .forEach(item -> listMap.merge(item.getRequest(), Arrays.asList(item), (prev, newOne) -> {
                    List<Item> res = new ArrayList<>();
                    res.addAll(prev);
                    res.addAll(newOne);

                    return res;
                }));
        return requests.stream()
                .map(request -> ItemRequestMapper.toDto(request, listMap.get(request)))
                .collect(Collectors.toList());
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto findById(@RequestHeader(USER_HEADER) Long userId,
                                           @PathVariable("requestId") Long id) {
        if (!userService.existsById(userId)) {
            throw new UserNotFoundException("No user by id " + userId);
        }
        ItemRequest itemRequest = itemRequestService.findById(id).orElseThrow(() -> new ItemRequestNotFoundException("No itemRequest by id " + id));
        List<Item> items = itemService.findAllByRequest(itemRequest);
        return ItemRequestMapper.toDto(itemRequest, items);
    }

    @GetMapping("/all")
    public List<ItemRequestResponseDto> findAllRequestsByOtherUsers(@RequestHeader(USER_HEADER) Long userId,
                                                                    @RequestParam(required = false, defaultValue = "0") Integer from,
                                                                    @RequestParam(required = false, defaultValue = "20") Integer size) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId));
        PageRequest page = PageRequest.of(from / size, size, Sort.by("created").descending());
        List<ItemRequest> itemRequests = itemRequestService.findAllOtherRequestors(user, page);

        Map<ItemRequest, List<Item>> itemMap = new HashMap<>();
        itemService.findAllByRequests(itemRequests)
                .forEach(item -> itemMap.merge(item.getRequest(), Arrays.asList(item), (prev, newOne) -> {
                    List<Item> res = new ArrayList<>();
                    res.addAll(prev);
                    res.addAll(newOne);

                    return res;
                }));

        return itemMap.entrySet()
                .stream()
                .map(entry -> ItemRequestMapper.toDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

}
