package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

class CommentMapperTest {

    @Test
    void toDto() {
        Comment comment = Comment.builder()
                .author(User.builder().name("name").email("test@mail.ru").build())
                .text("comment")
                .build();
        CommentResponseDto dto = CommentMapper.toDto(comment);
        assertNotNull(dto);
    }
}