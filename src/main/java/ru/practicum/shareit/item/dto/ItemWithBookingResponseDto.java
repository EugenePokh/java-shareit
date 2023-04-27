package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemWithBookingResponseDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private String request;

    private Booking lastBooking;
    private Booking nextBooking;

    @Data
    public static class Booking {
        private Long id;
        private Long bookerId;
    }
}
