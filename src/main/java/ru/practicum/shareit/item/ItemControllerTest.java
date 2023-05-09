package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemPostDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class ItemControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ItemService itemService;

    @MockBean
    private CommentService commentService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void postTest() throws Exception {
        ItemPostDto dto = new ItemPostDto();
        dto.setAvailable(true);
        dto.setDescription("desc");
        dto.setName("name");

        User user = User.builder()
                .id(1L)
                .name("name")
                .email("test@mail.ru")
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        Item item = Item.builder()
                .id(1L)
                .owner(user)
                .available(true)
                .name("name")
                .description("desc")
                .build();

        Mockito.when(itemService.save(Mockito.any()))
                .thenReturn(item);

        RequestBuilder requestBuilder = post("/items", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto));
        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void postUserNotFound() throws Exception {
        ItemPostDto dto = new ItemPostDto();
        dto.setAvailable(true);
        dto.setDescription("desc");
        dto.setName("name");
        dto.setRequestId(1L);

        RequestBuilder requestBuilder = post("/items", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto));
        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void postRequestNotFound() throws Exception {
        ItemPostDto dto = new ItemPostDto();
        dto.setAvailable(true);
        dto.setDescription("desc");
        dto.setName("name");
        dto.setRequestId(1L);

        User user = User.builder()
                .id(1L)
                .name("name")
                .email("test@mail.ru")
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        RequestBuilder requestBuilder = post("/items", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto));
        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void postCommentUserNotFound() throws Exception {
        CommentRequestDto dto = new CommentRequestDto();
        dto.setText("text");

        RequestBuilder requestBuilder = post("/items/{id}/comment", 1L)
                .header("X-Sharer-User-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto));
        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void postCommentItemNotFound() throws Exception {
        CommentRequestDto dto = new CommentRequestDto();
        dto.setText("text");

        Mockito.when(userService.findById(Mockito.any())).thenReturn(Optional.of(User.builder().build()));
        Mockito.when(itemService.findById(Mockito.any())).thenReturn(Optional.empty());

        RequestBuilder requestBuilder = post("/items/{id}/comment", 1L)
                .header("X-Sharer-User-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto));
        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void postComment() throws Exception {
        CommentRequestDto dto = new CommentRequestDto();
        dto.setText("text");

        User user = User.builder()
                .name("name")
                .build();

        Mockito.when(userService.findById(Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(itemService.findById(Mockito.any())).thenReturn(Optional.of(Item.builder().build()));
        Mockito.when(commentService.createCommentForItem(Mockito.any(), Mockito.any(), Mockito.eq("text"))).thenReturn(Comment.builder()
                .author(user)
                .build());

        RequestBuilder requestBuilder = post("/items/{id}/comment", 1L)
                .header("X-Sharer-User-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto));
        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void patchTest() throws Exception {
        ItemPostDto dto = new ItemPostDto();
        dto.setAvailable(true);
        dto.setDescription("desc");
        dto.setName("name");

        User user = User.builder()
                .id(1L)
                .name("name")
                .email("test@mail.ru")
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        Item item = Item.builder()
                .id(1L)
                .owner(user)
                .available(true)
                .name("name")
                .description("desc")
                .build();

        Mockito.when(itemService.findById(1L))
                .thenReturn(Optional.of(item));
        Mockito.when(itemService.save(Mockito.any()))
                .thenReturn(item);

        RequestBuilder requestBuilder = patch("/items/{id}", 1L)
                .header("X-Sharer-User-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto));
        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void patchUserNotFound() throws Exception {
        ItemPostDto dto = new ItemPostDto();
        dto.setAvailable(true);
        dto.setDescription("desc");
        dto.setName("name");
        dto.setRequestId(1L);

        RequestBuilder requestBuilder = patch("/items/{id}", 1L)
                .header("X-Sharer-User-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto));
        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void patchRequestNotFound() throws Exception {
        ItemPostDto dto = new ItemPostDto();
        dto.setAvailable(true);
        dto.setDescription("desc");
        dto.setName("name");
        dto.setRequestId(1L);

        User user = User.builder()
                .id(1L)
                .name("name")
                .email("test@mail.ru")
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        RequestBuilder requestBuilder = patch("/items/{id}", 1L)
                .header("X-Sharer-User-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto));
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

        RequestBuilder requestBuilder = get("/items", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L);
        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void findAllUserNotFound() throws Exception {
        RequestBuilder requestBuilder = get("/items", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L);
        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void findByIdUserNotFound() throws Exception {
        RequestBuilder requestBuilder = get("/items/{id}", 1L)
                .header("X-Sharer-User-Id", 1L);
        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void findByIdItemNotFound() throws Exception {
        User user = User.builder()
                .id(1L)
                .name("name")
                .email("test@mail.ru")
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        RequestBuilder requestBuilder = get("/items/{id}", 1L)
                .header("X-Sharer-User-Id", 1L);
        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void findById() throws Exception {
        User user = User.builder()
                .id(1L)
                .name("name")
                .email("test@mail.ru")
                .build();

        Mockito.when(userService.findById(1L))
                .thenReturn(Optional.of(user));

        Item item = Item.builder()
                .owner(user)
                .id(1L)
                .description("desc")
                .build();

        Mockito.when(itemService.findById(1L))
                .thenReturn(Optional.of(item));


        RequestBuilder requestBuilder = get("/items/{id}", 1L)
                .header("X-Sharer-User-Id", 1L);
        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void findAllByTextIsBlank() throws Exception {
        RequestBuilder requestBuilder = get("/items/search?text=", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L);
        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void findAllByText() throws Exception {
        RequestBuilder requestBuilder = get("/items/search?text=some", new ArrayList<>())
                .header("X-Sharer-User-Id", 1L);
        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

    }
}