package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.dto.UserPostDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Validated @RequestBody UserPostDto userDto) {
         User created = userService.create(UserMapper.toModel(userDto));
        return new ResponseEntity<>(UserMapper.toDto(created), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable Long id, @Validated @RequestBody UserPatchDto userDto) {
        Long finalId = id;
        User user = userService.findById(id).orElseThrow(() -> new UserNotFoundException("No user by id " + finalId));
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }

        return UserMapper.toDto(userService.update(user));
    }

    @GetMapping
    public List<UserResponseDto> findAll() {
        return userService.findAll().stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserResponseDto getById(@PathVariable Long id) {
        return UserMapper.toDto(userService.findById(id).orElseThrow(() -> new UserNotFoundException("No such user with id " + id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        User user = userService.findById(id).orElseThrow(() -> new UserNotFoundException("No such user with id " + id));
        userService.delete(user);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
