package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.dto.ItemPostDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemNotFoundException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private static final String USER_HEADER = "X-Sharer-User-Id";

    private final UserService userService;
    private final ItemService itemService;
    private final ItemMapper itemMapper;


    @PostMapping
    public ItemResponseDto post(@Valid @RequestBody ItemPostDto itemDto,
                                @Valid @NotNull @RequestHeader(USER_HEADER) Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId));
        Item item = toModel(itemDto, user);
        return itemMapper.toDto(itemService.save(item));
    }

    @PatchMapping("/{id}")
    public ItemResponseDto patch(@PathVariable Long id,
                                 @Valid @RequestBody ItemPatchDto itemDto,
                                 @Valid @NotNull @RequestHeader(USER_HEADER) Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId));
        Item item = itemService.findById(id).orElseThrow(() -> new ItemNotFoundException("No item by id " + id));
        Item patchItem = toModel(item, itemDto, user);
        return itemMapper.toDto(itemService.save(patchItem));
    }

    @GetMapping
    public List<ItemResponseDto> findAll(@Valid @NotNull @RequestHeader(USER_HEADER) Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException("No user by id " + userId));
        return itemService.findAllByUser(user)
                .stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ItemResponseDto findById(@PathVariable Long id) {
        return itemMapper.toDto(itemService.findById(id).orElseThrow(() -> new ItemNotFoundException("No item by id " + id)));
    }

    @GetMapping("/search")
    public List<ItemResponseDto> findAllByText(@RequestParam String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemService.findAll()
                .stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()) || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    private Item toModel(ItemPostDto itemDto, User user) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setAvailable(itemDto.getAvailable());
        item.setRequest(itemDto.getRequest());
        item.setDescription(itemDto.getDescription());
        item.setOwner(user);
        return item;
    }

    private Item toModel(Item item, ItemPatchDto itemDto, User user) {
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

        if (itemDto.getRequest() != null) {
            item.setRequest(itemDto.getRequest());
        }

        return item;
    }
}
