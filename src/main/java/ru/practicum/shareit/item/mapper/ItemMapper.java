package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.dto.ItemPostDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemWithBookingResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserNotFoundException;

public class ItemMapper {

    public static ItemResponseDto toDto(Item item) {
        ItemResponseDto itemDto = new ItemResponseDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setDescription(item.getDescription());
        return itemDto;
    }

    public static ItemWithBookingResponseDto toDtoWithBooking(Item item) {
        ItemWithBookingResponseDto itemDto = new ItemWithBookingResponseDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setDescription(item.getDescription());

        return itemDto;
    }

    public static Item toModel(ItemPostDto itemDto, User user) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setAvailable(itemDto.getAvailable());
        //todo
        //item.setRequest(itemDto.getRequest());
        item.setDescription(itemDto.getDescription());
        item.setOwner(user);
        return item;
    }

    public static Item toModel(Item item, ItemPatchDto itemDto, User user) {
        if (!item.getOwner().equals(user)) {
            throw new UserNotFoundException("No item owner");
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        if (itemDto.getRequest() != null) {
            //todo
            //item.setRequest(itemDto.getRequest());
        }

        return item;
    }
}
