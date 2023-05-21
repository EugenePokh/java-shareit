package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.dto.ItemPostDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {

    @Test
    void toDto() {
        Item item = Item.builder()
                .name("some name")
                .description("some description")
                .available(true)
                .build();

        ItemResponseDto dto = ItemMapper.toDto(item);
        assertNotNull(dto);
        assertEquals(item.getName(), dto.getName());
        assertEquals(item.getDescription(), dto.getDescription());
        assertEquals(item.getAvailable(), dto.getAvailable());
    }

    @Test
    void toDtoWithRequest() {
        Item item = Item.builder()
                .name("some name")
                .description("some description")
                .available(true)
                .request(ItemRequest.builder().build())
                .build();

        ItemResponseDto dto = ItemMapper.toDto(item);
        assertNotNull(dto);
        assertEquals(item.getName(), dto.getName());
        assertEquals(item.getDescription(), dto.getDescription());
        assertEquals(item.getAvailable(), dto.getAvailable());
    }

    @Test
    void toModel() {
        ItemPostDto dto = new ItemPostDto();
        dto.setAvailable(true);
        dto.setName("some name");
        dto.setDescription("some description");
        dto.setRequestId(null);

        User user = User.builder()
                .name("some")
                .email("test@mail.ru")
                .build();

        Item item = ItemMapper.toModel(dto, user, null);
        assertNotNull(item);
    }

    @Test
    void toModelWithRequest() {
        ItemPostDto dto = new ItemPostDto();
        dto.setAvailable(true);
        dto.setName("some name");
        dto.setDescription("some description");
        dto.setRequestId(null);

        User user = User.builder()
                .name("some")
                .email("test@mail.ru")
                .build();

        Item item = ItemMapper.toModel(dto, user, ItemRequest.builder().build());
        assertNotNull(item);
    }

    @Test
    void toModelSecond() {
        ItemPatchDto dto = new ItemPatchDto();
        dto.setAvailable(true);
        dto.setName("some name");
        dto.setDescription("some description");
        dto.setRequest("1");

        User user = User.builder()
                .name("some")
                .email("test@mail.ru")
                .build();

        Item current = Item.builder()
                .name("sss")
                .owner(user)
                .build();

        Item item = ItemMapper.toModel(current, dto, user);
        assertNotNull(item);
    }

    @Test
    void toModelSecondThrowException() {
        ItemPatchDto dto = new ItemPatchDto();
        dto.setAvailable(true);
        dto.setName("some name");
        dto.setDescription("some description");

        User user = User.builder()
                .name("some")
                .email("test@mail.ru")
                .build();

        Item current = Item.builder()
                .name("sss")
                .owner(User.builder().build())
                .build();

        assertThrows(UserNotFoundException.class, () -> {
                    ItemMapper.toModel(current, dto, user);
                }
        );
    }
}