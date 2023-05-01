package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Service
public class UserServiceInMemory implements UserService {

    Map<Long, User> users = new HashMap<>();
    private Long idCounter = 0L;

    @Override
    public void delete(User user) {
        users.remove(user.getId());
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.values()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(generateId());
        }

        users.put(user.getId(), user);

        return user;
    }

    private Long generateId() {
        idCounter++;
        return idCounter;
    }
}
