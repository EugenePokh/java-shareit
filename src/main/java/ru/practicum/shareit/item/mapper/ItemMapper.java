package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {


    public ItemDto toDto (Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setName(item.getName());
        itemDto.setRequest(item.getRequest());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setDescription(item.getDescription());
        return itemDto;
    }
}
