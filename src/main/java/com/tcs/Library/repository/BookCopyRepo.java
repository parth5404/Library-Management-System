package com.tcs.Library.repository;

import com.tcs.Library.entity.Book;
import com.tcs.Library.entity.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import com.tcs.Library.enums.BookStatus;

@Repository
public interface BookCopyRepo extends JpaRepository<BookCopy, Long> {
    Optional<BookCopy> findFirstByBookAndStatusIn(Book book, List<BookStatus> statuses);
}
