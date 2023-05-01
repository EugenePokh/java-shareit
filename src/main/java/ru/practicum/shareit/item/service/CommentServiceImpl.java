package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingValidationException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BookingService bookingService;

    @Override
    public List<Comment> findAllByItem(Item item) {
        return commentRepository.findAllByItem(item);
    }

    @Override
    public Comment createCommentForItem(Item item, User user, String text) {
        List<Booking> bookingList = bookingService.findAllByBookerAndStatus(user, Booking.Status.APPROVED)
                .stream()
                .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
        if (bookingList.size() == 0) {
            throw new BookingValidationException("No booking for user with id " + user.getId());
        }

        Comment comment = Comment.builder()
                .author(user)
                .item(item)
                .text(text)
                .created(LocalDateTime.now())
                .build();

        return commentRepository.save(comment);
    }
}
