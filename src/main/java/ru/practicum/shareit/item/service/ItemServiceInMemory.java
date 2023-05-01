package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemServiceInMemory implements ItemService {

    private final Map<Long, Item> items = new HashMap<>();
    private Long idCounter = 0L;

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Item save(Item item) {
        if (item.getId() == null) {
            item.setId(generateId());
        }
        items.put(item.getId(), item);

        return item;
    }

    @Override
    public List<Item> findAvailableByText(String text) {
        return null;
    }

    @Override
    public List<Item> findAllByUser(User user) {
        return items.values()
                .stream()
                .filter(item -> item.getOwner().equals(user))
                .collect(Collectors.toList());
    }

    private Long generateId() {
        idCounter++;
        return idCounter;
    }
}
