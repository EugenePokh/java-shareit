package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestMapperTest {

    @Test
    void toDto() {
        ItemRequest itemRequest = ItemRequest.builder()
                .description("some")
                .build();
        Item item = Item.builder().build();
        ItemRequestResponseDto dto = ItemRequestMapper.toDto(itemRequest, Arrays.asList(item));
        assertNotNull(dto);
    }

    @Test
    void toModel() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("dfdfd");

        ItemRequest itemRequest = ItemRequestMapper.toModel(
                dto,
                User.builder()
                        .email("test@mail.ru")
                        .name("some")
                        .build());

        assertNotNull(itemRequest);
    }
}