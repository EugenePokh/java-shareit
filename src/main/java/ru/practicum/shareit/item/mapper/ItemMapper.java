package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper {


    public ItemResponseDto toDto(Item item) {
        ItemResponseDto itemDto = new ItemResponseDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setRequest(item.getRequest());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setDescription(item.getDescription());
        return itemDto;
    }
}
