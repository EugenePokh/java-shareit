package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemServiceImplTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private TestEntityManager entityManager;

    @TestConfiguration
    static class ItemServiceImplTestContextConfiguration {

        @Bean
        public ItemService itemService(ItemRepository itemRepository) {
            return new ItemServiceImpl(itemRepository);
        }
    }

    @Test
    void findAllByRequest() {
        ItemRequest itemRequest = ItemRequest.builder()
                .description("some request")
                .build();
        itemRequest = entityManager.persistAndFlush(itemRequest);

        Item item = Item.builder()
                .name("test")
                .description("test description")
                .owner(null)
                .available(true)
                .request(itemRequest)
                .build();

        Item created = entityManager.persistAndFlush(item);

        List<Item> list = itemService.findAllByRequest(itemRequest);
        assertEquals(Arrays.asList(created), list);
    }

    @Test
    void findAllByUser() {
        User user = User.builder()
                .name("test")
                .email("test@mail.ri")
                .build();
        user = entityManager.persistAndFlush(user);

        Item item = Item.builder()
                .name("test")
                .description("test description")
                .owner(user)
                .available(true)
                .request(null)
                .build();

        Item created = entityManager.persistAndFlush(item);

        List<Item> list = itemService.findAllByUser(user);
        assertEquals(Arrays.asList(created), list);
    }

    @Test
    void save() {
        Item item = Item.builder()
                .name("test")
                .description("test description")
                .owner(null)
                .available(true)
                .request(null)
                .build();

        Item created = itemService.save(item);
        assertEquals(item, created);
    }

    @Test
    void findAvailableByText() {
        String text = "TesT";

        Item item1 = Item.builder()
                .id(1L)
                .name("Some test")
                .available(true)
                .description("")
                .build();

        Item item2 = Item.builder()
                .id(2L)
                .name("Test some")
                .available(true)
                .description("")
                .build();

        Item item3 = Item.builder()
                .id(3L)
                .name("Some ")
                .available(true)
                .description("test")
                .build();

        entityManager.merge(item1);
        entityManager.merge(item2);
        entityManager.merge(item3);

        List<Item> itemList = itemService.findAvailableByText(text);
        assertEquals(3, itemList.size());
    }

    @Test
    void findById() {
        Item item = Item.builder()
                .name("test")
                .description("test description")
                .owner(null)
                .available(true)
                .request(null)
                .build();
        item = entityManager.persistAndFlush(item);

        Optional<Item> created = itemService.findById(item.getId());
        assertEquals(Optional.of(item), created);
    }

    @Test
    void findAll() {
        Item item = Item.builder()
                .name("test")
                .description("test description")
                .owner(null)
                .available(true)
                .request(null)
                .build();
        entityManager.persistAndFlush(item);

        List<Item> list = itemService.findAll();
        assertEquals(Arrays.asList(item), list);

    }
}