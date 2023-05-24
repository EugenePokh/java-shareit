package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
public class ItemDtoLastNextBooking {
    private final Long id;
    private final Long bookerId;
}
