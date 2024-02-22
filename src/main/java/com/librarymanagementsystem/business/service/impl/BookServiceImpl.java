package com.librarymanagementsystem.business.service.impl;

import com.librarymanagementsystem.business.mappers.BookMapper;
import com.librarymanagementsystem.business.repository.BookRepository;
import com.librarymanagementsystem.business.repository.model.BookDAO;
import com.librarymanagementsystem.business.service.BookService;
import com.librarymanagementsystem.model.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookServiceImpl implements BookService {
    private final BookRepository repository;
    private final BookMapper bookMapper;

    @Autowired
    public BookServiceImpl(BookRepository repository, BookMapper bookMapper) {
        this.repository = repository;
        this.bookMapper = bookMapper;
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> list = repository.findAll()
                .stream()
                .map(bookMapper::daoToBook)
                .collect(Collectors.toList());
        log.info("Size of the Book list: {}", list.size());
        return list;
    }

    @Override
    public Optional<Book> findBookById(Long id) {
        Optional<BookDAO> customerDAO = repository.findById(id);
        if (customerDAO.isEmpty()) {
            log.info("Book with id {} does not exist.", id);
            return Optional.empty();
        }
        log.info("Book with id {} found.", id);
        return customerDAO.map(bookMapper::daoToBook);
    }
    @Override
    public Book saveBook(Book book) {
        log.info("Saving Book entry: {}", book);
        return bookMapper.daoToBook(repository.save(bookMapper.bookToDAO(book)));
    }

    @Override
    public void deleteBookById(Long id) {
        repository.deleteById(id);
        log.info("Book entry with id: {} is deleted", id);
    }
}
