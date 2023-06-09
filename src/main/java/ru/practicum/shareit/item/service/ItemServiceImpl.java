package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Service
@Primary
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public List<Item> findAllByRequest(ItemRequest request) {
        return itemRepository.findAllByRequest(request);
    }

    @Override
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public List<Item> findAvailableByText(String text) {
        return itemRepository.findAllByText(text);
    }

    @Override
    public List<Item> findAllByRequests(List<ItemRequest> itemRequests) {
        return itemRepository.findAllByRequestIn(itemRequests);
    }

    @Override
    public List<Item> findAllByUser(User user) {
        return itemRepository.findAllByOwner(user, Sort.by("id").ascending());
    }

    @Override
    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }
}
