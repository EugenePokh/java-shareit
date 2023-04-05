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

import javax.validation.ValidationException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;

    private User toModel(UserPostDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Validated @RequestBody UserPostDto userDto) {
        if (userService.findByEmail(userDto.getEmail()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        User created = userService.save(toModel(userDto));
        return new ResponseEntity<>(userMapper.toDto(created), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable Long id, @Validated @RequestBody UserPatchDto userDto) {
        Long finalId = id;
        User user = userService.findById(id).orElseThrow(() -> new UserNotFoundException("No user by id " + finalId));
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            Optional<User> userOpt = userService.findByEmail(userDto.getEmail());
            if (userOpt.isPresent()) {
                if (!Objects.equals(userOpt.get().getId(), id)) {
                    throw new ValidationException("same email occupied");
                }
            }
            user.setEmail(userDto.getEmail());
        }


        return userMapper.toDto(userService.save(user));
    }

    @GetMapping
    public List<UserResponseDto> findAll() {
        return userService.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserResponseDto getById(@PathVariable Long id) {
        return userMapper.toDto(userService.findById(id).orElseThrow(() -> new UserNotFoundException("No such user with id " + id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        User user = userService.findById(id).orElseThrow(() -> new UserNotFoundException("No such user with id " + id));
        userService.delete(user);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
