package ru.practicum.shareit.booking.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
