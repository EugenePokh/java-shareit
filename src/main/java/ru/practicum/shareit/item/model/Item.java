package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.booking.model.Request;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Data
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Boolean available;

    @JoinColumn(name = "owner_id")
    @ManyToOne
    private User owner;

    @JoinColumn(name = "request_id")
    @ManyToOne
    private Request request;
}
