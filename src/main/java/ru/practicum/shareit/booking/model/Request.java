package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Entity
@Table(name = "requests")
@Data
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String description;

    @JoinColumn(name = "requestor_id")
    @ManyToOne
    private User requestor;
}
