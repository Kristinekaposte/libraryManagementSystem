package com.librarymanagementsystem.web.controller;

import com.librarymanagementsystem.business.service.BookService;
import com.librarymanagementsystem.model.Book;
import com.librarymanagementsystem.openapi.DescriptionVariables;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = DescriptionVariables.BOOK, description = "Used to get and save Books")
@RequestMapping("api/v1/library/management/system/")
@Slf4j
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/allBooks")
    @Operation(
            summary = "Get All Books",
            description = "Retrieve all books from the database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The request has succeeded"),
                    @ApiResponse(responseCode = "500", description = "Server error")})
    public ResponseEntity<List<Book>> getAllBookEntries() {
        List<Book> list = bookService.getAllBooks();
        if (list.isEmpty()) {
            log.info("Empty Book list found");
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }
        log.info("List size: {}", list.size());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/getByID/{id}")
    @Operation(
            summary = "Find Book by id",
            description = "Retrieve book from the database by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The request has succeeded"),
                    @ApiResponse(responseCode = "404", description = "The server has not found anything matching the Request-URI"),
                    @ApiResponse(responseCode = "500", description = "Server error")})
    public ResponseEntity<Book> getBookById(@Parameter(description = "ID of the Book entry", required = true)
                                                             @PathVariable("id") Long id) {
        Optional<Book> bookOptional = bookService.findBookById(id);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            log.info("Found book with id {} ", id);
            return ResponseEntity.status(HttpStatus.OK).body(book);
        }
        log.warn("Book not found with id: {}", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).header(
                "Message", "Book not found with id: " + id).build();
    }

    @PostMapping("/saveBook")
    @Operation(
            summary = "Save a Book",
            description = "Saves a new book to the database",
            responses = {
                    @ApiResponse(responseCode = "201", description = "The book has been successfully created"),
                    @ApiResponse(responseCode = "400", description = "Bad Request, invalid book data"),
                    @ApiResponse(responseCode = "500", description = "Server error")})
    public ResponseEntity<Book> saveBook(@RequestBody Book book) {
        try {
            Book savedBook = bookService.saveBook(book);
            log.info("Book saved successfully: {}", savedBook);
            return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error saving book: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Deletes Book by id",
            description = "Provide an id to delete specific book entry from the database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The request has succeeded"),
                    @ApiResponse(responseCode = "404", description = "The server has not found anything matching the Request-URI"),
                    @ApiResponse(responseCode = "500", description = "Server error")})
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        if (bookService.findBookById(id).isPresent()) {
            bookService.deleteBookById(id);
            log.info("Book entry with ID: {} deleted", id);
            return ResponseEntity.ok("Book entry with ID " + id + " deleted");
        }
        log.warn("Cannot delete Book entry with ID: {}, entry not found", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book entry not found with ID: " + id);
    }
}
