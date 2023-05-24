package ru.practicum.shareit.user.service;


public class UserValidationException extends RuntimeException {
    public UserValidationException(String message) {
        super(message);
    }
}
