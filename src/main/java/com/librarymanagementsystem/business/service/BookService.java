package com.librarymanagementsystem.business.service;

import com.librarymanagementsystem.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> getAllBooks();

    Optional<Book> findBookById(Long id);

    Book saveBook(Book book);

    void deleteBookById(Long id);
}
