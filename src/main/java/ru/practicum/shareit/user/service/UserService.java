package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long id);
}
