package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long id);

    User create(User user);

    User update(User user);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    void delete(User user);

    boolean existsById(Long id);
}
