package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;

@Component
public class CommentMapper {

    public CommentResponseDto toDto(Comment comment) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setId(comment.getId());
        commentResponseDto.setText(comment.getText());
        commentResponseDto.setAuthorName(comment.getAuthor().getName());
        commentResponseDto.setCreated(comment.getCreated());

        return commentResponseDto;
    }

}
