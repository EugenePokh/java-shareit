package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookItemRequestDto {

	@NotNull
	private Long itemId;
	@NotNull
	@FutureOrPresent(message = "Дата бронирования должна быть в будущем или в настоящем времени")
	private LocalDateTime start;
	@NotNull
	@Future(message = "Дата конца бронирования не может быть в прошлом")
	private LocalDateTime end;
}
