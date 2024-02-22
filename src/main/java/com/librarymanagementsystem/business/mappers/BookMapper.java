package com.librarymanagementsystem.business.mappers;

import com.librarymanagementsystem.business.repository.model.BookDAO;
import com.librarymanagementsystem.model.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDAO bookToDAO(Book book);
    Book daoToBook(BookDAO bookDAO);
}
