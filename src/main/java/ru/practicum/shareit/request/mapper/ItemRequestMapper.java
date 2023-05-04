package ru.practicum.shareit.request.mapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class ItemRequestMapper {

    public static ItemRequestResponseDto toDto(ItemRequest itemRequest) {
       ItemRequestResponseDto itemRequestResponseDto = new ItemRequestResponseDto();
       itemRequestResponseDto.setId(itemRequest.getId());
       itemRequestResponseDto.setDescription(itemRequest.getDescription());
       itemRequestResponseDto.setRequestor(itemRequest.getRequestor());
       itemRequestResponseDto.setCreated(itemRequest.getCreated());
       return itemRequestResponseDto;
    }

    public static ItemRequest toModel(ItemRequestDto itemRequestDto, User user) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequest;
    }
}
