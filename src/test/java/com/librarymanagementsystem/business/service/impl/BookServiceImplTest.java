package com.librarymanagementsystem.business.service.impl;

import com.librarymanagementsystem.business.mappers.BookMapper;
import com.librarymanagementsystem.business.repository.BookRepository;
import com.librarymanagementsystem.business.repository.model.BookDAO;
import com.librarymanagementsystem.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookDAO bookDAO;
    private List<BookDAO> bookDAOList;

    @BeforeEach
    public void init() {
        book = new Book(1L, "Author Name", "Book Title", 19.99, "Description for Book");
        bookDAO = new BookDAO(1L, "Author Name", "Book Title", 19.99, "Description for Book");
        bookDAOList =createBookDAOList(bookDAO);
    }

    @Test
    void testGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(bookDAOList);
        when(bookMapper.daoToBook(bookDAO)).thenReturn(book);

        List<Book> bookList = bookService.getAllBooks();

        verify(bookRepository, times(1)).findAll();
        verify(bookMapper, times(bookDAOList.size())).daoToBook(any(BookDAO.class));
        assertEquals(bookDAOList.size(), bookList.size());
        assertEquals(book.getId(), bookList.get(0).getId());
    }

    @Test
    void testGetAllBooks_ListEmpty() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        List<Book> result = bookService.getAllBooks();

        verify(bookRepository, times(1)).findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindBookById_Found() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookDAO));
        when(bookMapper.daoToBook(bookDAO)).thenReturn(book);

        Optional<Book> actualResult = bookService.findBookById(1L);

        assertTrue(actualResult.isPresent());
        assertEquals(book, actualResult.get());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookMapper, times(1)).daoToBook(bookDAO);
    }

    @Test
    void testFindBookById_NotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Book> result = bookService.findBookById(99L);

        assertFalse(result.isPresent());
        verify(bookRepository, times(1)).findById(anyLong());
    }

    @Test
    void testSaveBook() {
        when(bookMapper.bookToDAO(book)).thenReturn(bookDAO);
        when(bookRepository.save(bookDAO)).thenReturn(bookDAO);
        when(bookMapper.daoToBook(bookDAO)).thenReturn(book);

        Book savedBook = bookService.saveBook(book);

        verify(bookRepository, times(1)).save(bookDAO);
        verify(bookMapper, times(1)).bookToDAO(book);
        verify(bookMapper,times(1)).daoToBook(bookDAO);
        assertNotNull(savedBook);
        assertEquals(book.getTitle(), savedBook.getTitle());
    }

    @Test
    void testDeleteBookById() {
        Long bookId = book.getId();

        bookService.deleteBookById(bookId);

        verify(bookRepository).deleteById(bookId);
    }

private List<BookDAO> createBookDAOList(BookDAO bookDAO) {
    List<BookDAO> list = new ArrayList<>();
    list.add(bookDAO);
    list.add(bookDAO);
    return list;
    }
}

