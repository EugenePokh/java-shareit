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
        assertNotNull(user);
    }

    @Test
    void toDto() {
        User user = User.builder()
                .name("some")
                .email("test@mail.ru")
                .build();
        UserResponseDto dto = UserMapper.toDto(user);
        assertNotNull(dto);
    }
}