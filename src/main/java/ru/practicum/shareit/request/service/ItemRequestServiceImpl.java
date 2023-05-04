package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final RequestRepository requestRepository;

    @Override
    public ItemRequest create(ItemRequest itemRequest) {
        return requestRepository.save(itemRequest);
    }

    @Override
    public List<ItemRequest> findAllByRequestor(User user) {
        return requestRepository.findAllByRequestor(user);
    }

    @Override
    public Optional<ItemRequest> findById(Long id) {
        return requestRepository.findById(id);
    }
}
