package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    Item save(Item item);

    List<Item> findAllByUser(User user);

    Optional<Item> findById(Long id);

    List<Item> findAll();
}
