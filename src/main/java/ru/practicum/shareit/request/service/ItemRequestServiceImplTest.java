package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRequestServiceImplTest {

    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private TestEntityManager entityManager;

    @TestConfiguration
    static class ItemRequestServiceImplTestContextConfiguration {

        @Bean
        public ItemRequestService itemRequestService(RequestRepository requestRepository) {
            return new ItemRequestServiceImpl(requestRepository);
        }
    }

    @Test
    void create() {
        String text = "some description";
        ItemRequest created = itemRequestService.create(ItemRequest.builder()
                .description(text)
                .build());

        assertNotNull(created);
        assertEquals(text, created.getDescription());
    }

    @Test
    void findAllByRequestor() {
        User user = entityManager.persistAndFlush(User.builder()
                .name("name")
                .email("test@mail.ru")
                .build());

        ItemRequest itemRequest = entityManager.persistAndFlush(ItemRequest.builder()
                .requestor(user)
                .description("some")
                .build());

        List<ItemRequest> list = itemRequestService.findAllByRequestor(user, Sort.by("created").descending());
        assertEquals(Arrays.asList(itemRequest), list);

    }

    @Test
    void findAllOtherRequestors() {
        User user = entityManager.persistAndFlush(User.builder()
                .name("name")
                .email("test@mail.ru")
                .build());

        User user1 = entityManager.persistAndFlush(User.builder()
                .name("name")
                .email("another@mail.ru")
                .build());

        ItemRequest itemRequest = entityManager.persistAndFlush(ItemRequest.builder()
                .requestor(user)
                .description("some")
                .build());

        List<ItemRequest> list = itemRequestService.findAllOtherRequestors(user1, PageRequest.of(0, 10));
        assertEquals(Arrays.asList(itemRequest), list);
    }

    @Test
    void findById() {
        ItemRequest request = entityManager.persistAndFlush(ItemRequest.builder()
                .description("some description")
                .build());

        Optional<ItemRequest> created = itemRequestService.findById(request.getId());
        assertEquals(Optional.of(request), created);
    }


}