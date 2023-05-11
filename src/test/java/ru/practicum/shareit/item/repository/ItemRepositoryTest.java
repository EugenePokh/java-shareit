package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void findAllByText() {
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

        List<Item> itemList = itemRepository.findAllByText(text);
        assertEquals(3, itemList.size());

    }
}