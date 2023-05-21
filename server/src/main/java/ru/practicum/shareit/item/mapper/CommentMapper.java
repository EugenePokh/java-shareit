package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;

public class CommentMapper {

    public static CommentResponseDto toDto(Comment comment) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setId(comment.getId());
        commentResponseDto.setText(comment.getText());
        commentResponseDto.setAuthorName(comment.getAuthor().getName());
        commentResponseDto.setCreated(comment.getCreated());

        return commentResponseDto;
    }

}
