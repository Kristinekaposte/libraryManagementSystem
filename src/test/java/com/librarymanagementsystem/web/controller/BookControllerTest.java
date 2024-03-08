package com.librarymanagementsystem.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.librarymanagementsystem.business.service.BookService;
import com.librarymanagementsystem.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private Book book;
    private Book savedBook;

    private static final String BASE_URL = "/api/v1/library/management/system";
    private static final String ALL_BOOKS_URL = BASE_URL + "/allBooks";
    private static final String GET_BY_ID_URL = BASE_URL + "/getByID/";
    private static final String SAVE_BOOK_URL = BASE_URL + "/saveBook";
    private static final String DELETE_BOOK_URL = BASE_URL + "/delete/";

    @BeforeEach
    public void init() {
        book = new Book(1L, "Author Name", "Book Title", 19.99, "Description for Book");
        savedBook = new Book(null, "Author Name", "Book Title", 19.99, "Description for Book");
    }

    @Test
    void testGetAllBookEntries_Successful() throws Exception {
        when(bookService.getAllBooks()).thenReturn(Collections.singletonList(book));
        mockMvc.perform(get(ALL_BOOKS_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(book.getId()))
                .andExpect(jsonPath("$[0].author").value(book.getAuthor()))
                .andExpect(jsonPath("$[0].title").value(book.getTitle()))
                .andExpect(jsonPath("$[0].price").value(book.getPrice()))
                .andExpect(jsonPath("$[0].description").value(book.getDescription()));
        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void testFindAllBooks_WhenListEmpty() throws Exception {
        when(bookService.getAllBooks()).thenReturn(Collections.emptyList());
        mockMvc.perform(get(ALL_BOOKS_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void testGetBookById_Found() throws Exception {
        when(bookService.findBookById(1L)).thenReturn(Optional.of(book));
        mockMvc.perform(get(GET_BY_ID_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(book.getId()))
                .andExpect(jsonPath("$.author").value(book.getAuthor()));
        verify(bookService, times(1)).findBookById(1L);
    }

    @Test
    void testGetBookById_NotFound() throws Exception {
        Long id = 99L;
        when(bookService.findBookById(id)).thenReturn(Optional.empty());
        mockMvc.perform(get(GET_BY_ID_URL + id))
                .andExpect(status().isNotFound())
                .andExpect(header().string("Message", "Book not found with id: " + 99));
        verify(bookService, times(1)).findBookById(id);
    }

    @Test
    void testSaveBook_Successful() throws Exception {
        when(bookService.saveBook(any())).thenReturn(savedBook);
        mockMvc.perform(post(SAVE_BOOK_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedBook)))
                .andExpect(status().isCreated())

                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(savedBook.getId()))
                .andExpect(jsonPath("$.author").value(savedBook.getAuthor()))
                .andExpect(jsonPath("$.title").value(savedBook.getTitle()))
                .andExpect(jsonPath("$.price").value(savedBook.getPrice()))
                .andExpect(jsonPath("$.description").value(savedBook.getDescription()));
        verify(bookService, times(1)).saveBook(savedBook);
    }

    @Test
    void testSaveBook_Exception() throws Exception {
        doThrow(new RuntimeException("Database error")).when(bookService).saveBook(any());

        mockMvc.perform(post(SAVE_BOOK_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedBook)))
                .andExpect(status().isInternalServerError());

        verify(bookService, times(1)).saveBook(any());
    }

    @Test
    void testDeleteBook_FoundAndDeleted() throws Exception {
        when(bookService.findBookById(book.getId())).thenReturn(Optional.of(book));
        doNothing().when(bookService).deleteBookById(book.getId());

        mockMvc.perform(delete(DELETE_BOOK_URL + book.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Book entry with ID " + book.getId() + " deleted")));
        verify(bookService, times(1)).findBookById(book.getId());
        verify(bookService, times(1)).deleteBookById(book.getId());
    }

    @Test
    void testDeleteBook_NotFound() throws Exception {
        Long id = 99L;
        when(bookService.findBookById(id)).thenReturn(Optional.empty());

        mockMvc.perform(delete(DELETE_BOOK_URL + id))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Book entry not found with ID: " + id)));
        verify(bookService, times(1)).findBookById(id);
        verify(bookService, never()).deleteBookById(anyLong());
    }
}

