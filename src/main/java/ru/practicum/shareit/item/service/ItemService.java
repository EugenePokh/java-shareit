package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    Item save(Item item);

    List<Item> findAllByUser(User user);

    List<Item> findAllByRequest(ItemRequest request);

    Optional<Item> findById(Long id);

    List<Item> findAll();

    List<Item> findAvailableByText(String text);

    List<Item> findAllByRequests(List<ItemRequest> itemRequests);
}
