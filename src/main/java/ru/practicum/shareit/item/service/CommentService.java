package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface CommentService {

    Comment createCommentForItem(Item item, User user, String comment);

    List<Comment> findAllByItem(Item item);

    List<Comment> findAllByItemIn(List<Item> items);
}
