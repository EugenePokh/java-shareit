package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingValidationException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private TestEntityManager entityManager;

    @TestConfiguration
    static class CommentServiceImplTestContextConfiguration {

        @Bean
        public CommentService commentService(CommentRepository commentRepository, BookingService bookingService) {
            return new CommentServiceImpl(commentRepository, bookingService);
        }
    }

    @Test
    void findAllByItem() {
        Item item = Item.builder()
                .name("some item")
                .description("description")
                .available(true)
                .build();
        item = entityManager.persistAndFlush(item);

        Comment comment = Comment.builder()
                .text("some text")
                .author(null)
                .item(item)
                .created(LocalDateTime.now())
                .build();
        Comment created = entityManager.persistAndFlush(comment);

        List<Comment> list = commentService.findAllByItem(item);
        assertEquals(Arrays.asList(created), list);
    }

    @Test
    void createCommentForItem() {
        String comment = "some comment";

        Item item = Item.builder()
                .name("some item")
                .description("description")
                .available(true)
                .build();
        item = entityManager.persistAndFlush(item);

        User user = User.builder()
                .name("name")
                .email("email@email.com")
                .build();
        Mockito.when(bookingService.findAllPastByBookerAndStatus(user, Booking.Status.APPROVED))
                .thenReturn(Arrays.asList(Booking.builder().build()));

        Comment created = commentService.createCommentForItem(item, user, comment);
        assertNotNull(created);
        assertEquals(comment, created.getText());

    }

    @Test
    void createCommentForItemThrowException() {
        String comment = "some comment";

        Item item = entityManager.persistAndFlush(Item.builder()
                .name("some item")
                .description("description")
                .available(true)
                .build());

        User user = User.builder()
                .name("name")
                .email("email@email.com")
                .build();
        Mockito.when(bookingService.findAllPastByBookerAndStatus(user, Booking.Status.APPROVED))
                .thenReturn(new ArrayList<>());
        assertThrows(BookingValidationException.class, () -> {
            commentService.createCommentForItem(item, user, comment);
        });
    }
}