package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestMapperTest {

    @Test
    void toDto() {
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("some")
                .created(LocalDateTime.now())
                .build();
        Item item = Item.builder().build();
        ItemRequestResponseDto dto = ItemRequestMapper.toDto(itemRequest, Arrays.asList(item));
        assertEquals(itemRequest.getDescription(), dto.getDescription());
        assertEquals(itemRequest.getCreated(), dto.getCreated());
        assertEquals(itemRequest.getId(), dto.getId());
    }

    @Test
    void toModel() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("dfdfd");

        ItemRequest itemRequest = ItemRequestMapper.toModel(
                dto,
                User.builder()
                        .id(1L)
                        .email("test@mail.ru")
                        .name("some")
                        .build());

        assertEquals(dto.getDescription(), itemRequest.getDescription());
    }
}