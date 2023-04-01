package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final Map<Long, Item> items = new HashMap<>();
    private Long idCounter = 0L;
    @Override
    public Item save(Item item) {
        item.setId(generateId());
        items.put(item.getId(), item);

        return item;
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
