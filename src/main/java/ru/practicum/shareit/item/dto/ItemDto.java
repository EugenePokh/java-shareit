package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */

@Data
public class ItemDto {
    @NotBlank
    private String name;
    private String description;
    private Boolean available;
    private String request;

}
