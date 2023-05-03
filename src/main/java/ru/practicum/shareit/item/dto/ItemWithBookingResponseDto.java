package ru.practicum.shareit.item.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class ItemWithBookingResponseDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private String request;

    private Booking lastBooking;
    private Booking nextBooking;

    private List<CommentResponseDto> comments = new ArrayList<>();

    @Data
    public static class Booking {
        private Long id;
        private Long bookerId;
    }
}
