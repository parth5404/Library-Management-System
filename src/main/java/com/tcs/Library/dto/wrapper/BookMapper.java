
package com.tcs.Library.dto.wrapper;

import java.util.HashSet;
import java.util.Set;

import com.tcs.Library.dto.BookDTO;
import com.tcs.Library.entity.Author;
import com.tcs.Library.entity.Book;

public class BookMapper {

    private BookMapper() {}

    public static Book toEntity(BookDTO dto, Set<Author> authors) {
        Book book = new Book();
        // book.setId(dto.getId());
        book.setBook_title(dto.getBookTitle());
        book.setCategory(dto.getCategory());
        book.setCoverUrl(dto.getCoverUrl());
        // book.setStatus(dto.getStatus());
        book.setQuantity(dto.getQuantity());
        book.setAuthor(authors == null ? new HashSet<>() : authors);
        return book;
    }
}
