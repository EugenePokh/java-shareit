package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface ItemRequestService {
    ItemRequest create(ItemRequest itemRequest);

    List<ItemRequest> findAllByRequestor(User user);

    Optional<ItemRequest> findById(Long id);

    List<ItemRequest> findAllOtherRequestors(User user);
}
