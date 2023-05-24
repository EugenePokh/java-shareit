package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserPostDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void toModel() {
        UserPostDto dto = new UserPostDto();
        dto.setName("some");
        dto.setEmail("test@mail.ru");
        User user = UserMapper.toModel(dto);
        assertEquals(dto.getName(), user.getName());
        assertEquals(dto.getEmail(), user.getEmail());
    }

    @Test
    void toDto() {
        User user = User.builder()
                .id(1L)
                .name("some")
                .email("test@mail.ru")
                .build();
        UserResponseDto dto = UserMapper.toDto(user);
        assertEquals(user.getName(), dto.getName());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getId(), dto.getId());
    }
}