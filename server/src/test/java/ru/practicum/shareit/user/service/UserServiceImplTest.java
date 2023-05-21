package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private TestEntityManager entityManager;

    @TestConfiguration
    static class UserServiceImplTestContextConfiguration {

        @Bean
        public UserService userService(UserRepository userRepository) {
            return new UserServiceImpl(userRepository);
        }
    }

    @Test
    void findById() {
        User user = User.builder()
                .name("test name")
                .email("test@mail.ru")
                .build();
        user = entityManager.persistAndFlush(user);

        Optional<User> created = userService.findById(user.getId());
        assertEquals(Optional.of(user), created);
    }

    @Test
    void create() {
        User user = User.builder()
                .name("test name")
                .email("test@mail.ru")
                .build();
        User created = userService.create(user);
        assertEquals(user, created);
    }

    @Test
    void update() {
        User user = User.builder()
                .name("test name")
                .email("test@mail.ru")
                .build();
        user = entityManager.persistAndFlush(user);

        user.setName("test name 1");
        User created = userService.update(user);
        assertEquals(user, created);
    }

    @Test
    void updateThrowException() {
        User user1 = entityManager.persistAndFlush(User.builder()
                .name("test name1")
                .email("test1@mail.ru")
                .build());

        User user2 = entityManager.persistAndFlush(User.builder()
                .name("test name2")
                .email("test2@mail.ru")
                .build());


        List<User> list = userService.findAll();

        assertThrows(ValidationException.class, () -> {
            userService.update(User.builder()
                    .id(user1.getId())
                    .name("test name1")
                    .email("test2@mail.ru")
                    .build());
        });
    }

    @Test
    void findByEmail() {
        User user = User.builder()
                .name("test name")
                .email("test@mail.ru")
                .build();
        user = entityManager.persistAndFlush(user);

        Optional<User> created = userService.findByEmail(user.getEmail());
        assertEquals(Optional.of(user), created);
    }

    @Test
    void findAll() {
        User user = User.builder()
                .name("test name")
                .email("test@mail.ru")
                .build();
        user = entityManager.persistAndFlush(user);

        List<User> list = userService.findAll();
        assertEquals(Arrays.asList(user), list);
    }

    @Test
    void delete() {
        User user = User.builder()
                .name("test name")
                .email("test@mail.ru")
                .build();
        user = entityManager.persistAndFlush(user);

        userService.delete(user);
        Optional<User> created = userService.findById(user.getId());
        assertTrue(created.isEmpty());

    }
}