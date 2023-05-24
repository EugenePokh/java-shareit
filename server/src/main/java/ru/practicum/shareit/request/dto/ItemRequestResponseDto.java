package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ItemRequestResponseDto {
    private Long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
    private List<ItemResponseDto> items = new ArrayList<>();
}
