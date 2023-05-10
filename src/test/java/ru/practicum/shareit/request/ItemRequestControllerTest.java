package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemRequestService itemRequestService;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void postNotFound() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("derc");

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.empty());

        RequestBuilder requestBuilder = post("/requests", new ArrayList<>())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(itemRequestDto))
                .header("X-Sharer-User-Id", 1L);

        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void postTest() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("derc");

        String name = "name";
        String email = "test@mail.ru";
        User user = User.builder()
                .id(1L)
                .name(name)
                .email(email)
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        Mockito.when(itemRequestService.create(Mockito.any())).thenReturn(ItemRequest.builder()
                .id(1L)
                .build());

        RequestBuilder requestBuilder = post("/requests", new ArrayList<>())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(itemRequestDto))
                .header("X-Sharer-User-Id", 1L);

        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();
    }


    @Test
    void findAllNotFound() throws Exception {
        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.empty());

        RequestBuilder requestBuilder = get("/requests", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L);
        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void findAll() throws Exception {
        User user = User.builder()
                .id(1L)
                .name("name")
                .email("test@mail.ru")
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        RequestBuilder requestBuilder = get("/requests", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L);
        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void findById() throws Exception {
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("some desc")
                .build();

        User user = User.builder()
                .id(1L)
                .name("name")
                .email("test@mail.ru")
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        Mockito.when(itemRequestService.findById(1L))
                .thenReturn(Optional.of(itemRequest));

        RequestBuilder requestBuilder = get("/requests/{id}", 1L)
                .header("X-Sharer-User-Id", 1L);
        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        assertNotNull(result.getResponse().getContentAsString());
    }

    @Test
    void findByIdUserNotFound() throws Exception {
        RequestBuilder requestBuilder = get("/requests/{id}", 1L)
                .header("X-Sharer-User-Id", 1L);
        mvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    void findByIdRequestNotFound() throws Exception {
        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.empty());

        RequestBuilder requestBuilder = get("/requests/{id}", 1L)
                .header("X-Sharer-User-Id", 1L);
        mvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllRequestsByOtherUsersNotFound() throws Exception {
        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.empty());

        RequestBuilder requestBuilder = get("/requests/all", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L);
        mvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllRequestsByOtherUsers() throws Exception {
        User user = User.builder()
                .id(1L)
                .name("name")
                .email("test@mail.ru")
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        RequestBuilder requestBuilder = get("/requests/all", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L);
        mvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }
}