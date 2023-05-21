package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CommentMapperTest {

    @Test
    void toDto() {
        Comment comment = Comment.builder()
                .id(1L)
                .author(User.builder().name("name").email("test@mail.ru").build())
                .text("comment")
                .created(LocalDateTime.now())
                .build();

        CommentResponseDto dto = CommentMapper.toDto(comment);

        assertEquals(comment.getId(), dto.getId());
        assertEquals(comment.getCreated(), dto.getCreated());
        assertEquals(comment.getText(), dto.getText());
        assertEquals("name", dto.getAuthorName());
    }
}