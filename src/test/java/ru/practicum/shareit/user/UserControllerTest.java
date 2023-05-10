package ru.practicum.shareit.user;

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
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.dto.UserPostDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createUser() throws Exception {
        String name = "test";
        String email = "test@mail.ru";

        Mockito.when(userService.create(User.builder()
                        .name(name)
                        .email(email)
                        .build()))
                .thenReturn(User.builder()
                        .name(name)
                        .email(email)
                        .id(1L).build());

        UserPostDto userPostDto = new UserPostDto();
        userPostDto.setName(name);
        userPostDto.setEmail(email);

        RequestBuilder requestBuilder = post("/users", new ArrayList<>())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userPostDto));

        mvc.perform(requestBuilder)
                .andExpect(status().isCreated());
    }

    @Test
    void updateUserNotFound() throws Exception {
        String name = "test";
        String email = "test@mail.ru";

        UserPatchDto dto = new UserPatchDto();
        dto.setName(name);
        dto.setEmail(email);

        RequestBuilder requestBuilder = patch("/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(dto));

        mvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser() throws Exception {
        String name = "test";
        String email = "test@mail.ru";

        User user = User.builder()
                .id(1L)
                .name(name)
                .email(email)
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        Mockito.when(userService.update(user))
                .thenReturn(user);

        UserPatchDto dto = new UserPatchDto();
        dto.setName(name);
        dto.setEmail(email);

        RequestBuilder requestBuilder = patch("/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(dto));

        mvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void findAllEmptyList() throws Exception {
        RequestBuilder requestBuilder = get("/users", new ArrayList<>());
        mvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    @Test
    void getById() throws Exception {
        User user = User.builder()
                .id(1L)
                .name("name")
                .email("test@mail.ru")
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        RequestBuilder requestBuilder = get("/users/{id}", 1L);
        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(objectMapper.writeValueAsString(user), result.getResponse().getContentAsString());

    }

    @Test
    void getByIdNotFound() throws Exception {
        RequestBuilder requestBuilder = get("/users/{id}", 1L);
        mvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteByIdNotFound() throws Exception {
        RequestBuilder requestBuilder = delete("/users/{id}", 1L);
        mvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteById() throws Exception {
        User user = User.builder()
                .id(1L)
                .name("name")
                .email("test@mail.ru")
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        RequestBuilder requestBuilder = delete("/users/{id}", 1L);
        mvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
    }
}