package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class BookingControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ItemService itemService;

    @MockBean
    private BookingService bookingService;

    private ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Test
    void postTestWrongDate() throws Exception {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(1L);
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now());

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(User.builder().build()));


        RequestBuilder requestBuilder = post("/bookings", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto));

        mvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

    }

    @Test
    void postTestItemNotAvailable() throws Exception {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(1L);
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        Item item = Item.builder()
                .available(false)
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(User.builder().build()));

        Mockito.when(itemService.findById(1L))
                .thenReturn(Optional.of(item));

        RequestBuilder requestBuilder = post("/bookings", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto));

        mvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

    }

    @Test
    void postTest() throws Exception {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(1L);
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        Item item = Item.builder()
                .available(true)
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(User.builder().build()));

        Mockito.when(itemService.findById(1L))
                .thenReturn(Optional.of(item));

        Mockito.when(bookingService.createReservation(Mockito.any()))
                .thenReturn(Booking.builder()
                        .item(item)
                        .build());

        RequestBuilder requestBuilder = post("/bookings", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto));

        mvc.perform(requestBuilder)
                .andExpect(status().isOk());

    }

    @Test
    void postTestUserNotFound() throws Exception {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(1L);
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        RequestBuilder requestBuilder = post("/bookings", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto));

        mvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    void postTestItemNotFound() throws Exception {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(1L);
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(User.builder().build()));

        RequestBuilder requestBuilder = post("/bookings", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto));

        mvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    void patchNotFound() throws Exception {
        RequestBuilder requestBuilder = patch("/bookings/{id}?approved=true", 1L)
                .header("X-Sharer-User-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    void patchBookingNotFound() throws Exception {
        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(User.builder().build()));

        RequestBuilder requestBuilder = patch("/bookings/{id}?approved=true", 1L)
                .header("X-Sharer-User-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    void patchTest() throws Exception {
        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(User.builder().build()));

        Mockito.when(bookingService.findById(1L))
                .thenReturn(Optional.of(Booking.builder().build()));

        Mockito.when(bookingService.decideReservation(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Booking.builder().build());

        RequestBuilder requestBuilder = patch("/bookings/{id}?approved=true", 1L)
                .header("X-Sharer-User-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void findByIdUserNotFound() throws Exception {
        RequestBuilder requestBuilder = get("/bookings/{id}", 1L)
                .header("X-Sharer-User-Id", 1L);
        mvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    void findByIdBookingNotFound() throws Exception {
        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(User.builder().build()));

        RequestBuilder requestBuilder = get("/bookings/{id}", 1L)
                .header("X-Sharer-User-Id", 1L);
        mvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    void findByIdWrongUser() throws Exception {
        User user = User.builder()
                .id(1L)
                .build();

        User user1 = User.builder()
                .id(2L)
                .build();

        Booking booking = Booking.builder()
                .id(1L)
                .booker(user1)
                .item(Item.builder()
                        .owner(User.builder().build())
                        .build())
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        Mockito.when(bookingService.findById(1L))
                .thenReturn(Optional.of(booking));

        RequestBuilder requestBuilder = get("/bookings/{id}", 1L)
                .header("X-Sharer-User-Id", 1L);
        mvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    void findById() throws Exception {
        User user = User.builder()
                .id(1L)
                .build();

        Booking booking = Booking.builder()
                .id(1L)
                .booker(user)
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        Mockito.when(bookingService.findById(1L))
                .thenReturn(Optional.of(booking));

        RequestBuilder requestBuilder = get("/bookings/{id}", 1L)
                .header("X-Sharer-User-Id", 1L);
        mvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void findAllUserNotFound() throws Exception {
        RequestBuilder requestBuilder = get("/bookings", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L);
        mvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllWrongState() throws Exception {
        User user = User.builder()
                .id(1L)
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        RequestBuilder requestBuilder = get("/bookings?state=WRONG", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L);
        mvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAll() throws Exception {
        User user = User.builder()
                .id(1L)
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        RequestBuilder requestBuilder = get("/bookings", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L);
        mvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void findAllCurrent() throws Exception {
        User user = User.builder()
                .id(1L)
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        RequestBuilder requestBuilder = get("/bookings?state=CURRENT", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L);
        mvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void findAllPast() throws Exception {
        User user = User.builder()
                .id(1L)
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        RequestBuilder requestBuilder = get("/bookings?state=PAST", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L);
        mvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void findAllFuture() throws Exception {
        User user = User.builder()
                .id(1L)
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        RequestBuilder requestBuilder = get("/bookings?state=FUTURE", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L);
        mvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void findAllWaiting() throws Exception {
        User user = User.builder()
                .id(1L)
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        RequestBuilder requestBuilder = get("/bookings?state=WAITING", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L);
        mvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void findAllRejected() throws Exception {
        User user = User.builder()
                .id(1L)
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        RequestBuilder requestBuilder = get("/bookings?state=REJECTED", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L);
        mvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void findAllByOwnerUserNotFound() throws Exception {
        RequestBuilder requestBuilder = get("/bookings/owner", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L);
        mvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllByOwnerWrongState() throws Exception {
        User user = User.builder()
                .id(1L)
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        RequestBuilder requestBuilder = get("/bookings/owner?state=WRONG", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L);
        mvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAllByOwner() throws Exception {
        User user = User.builder()
                .id(1L)
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        RequestBuilder requestBuilder = get("/bookings/owner", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L);
        mvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void findAllByOwnerCurrent() throws Exception {
        User user = User.builder()
                .id(1L)
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        RequestBuilder requestBuilder = get("/bookings/owner?state=CURRENT", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L);
        mvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void findAllByOwnerPast() throws Exception {
        User user = User.builder()
                .id(1L)
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        RequestBuilder requestBuilder = get("/bookings/owner?state=PAST", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L);
        mvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void findAllByOwnerFuture() throws Exception {
        User user = User.builder()
                .id(1L)
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        RequestBuilder requestBuilder = get("/bookings/owner?state=FUTURE", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L);
        mvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void findAllByOwnerWaiting() throws Exception {
        User user = User.builder()
                .id(1L)
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        RequestBuilder requestBuilder = get("/bookings/owner?state=WAITING", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L);
        mvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void findAllByOwnerRejected() throws Exception {
        User user = User.builder()
                .id(1L)
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        RequestBuilder requestBuilder = get("/bookings/owner?state=REJECTED", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L);
        mvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

}