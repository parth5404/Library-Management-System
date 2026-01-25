package com.tcs.Library.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tcs.Library.enums.IssueStatus;

@Getter
@Setter
@Entity
@Table(name = "issued_books")
public class IssuedBooks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_copy_id", nullable = false)
    private BookCopy bookCopy;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    private LocalDate returnDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IssueStatus status; 

    @Column(precision = 10, scale = 2)
    private BigDecimal fineAmount = BigDecimal.ZERO;

    // Additional computed properties for frontend convenience
    @JsonProperty("borrowedDate")
    public LocalDate getBorrowedDate() {
        return issueDate;
    }

    @JsonProperty("book")
    public Book getBook() {
        return bookCopy != null ? bookCopy.getBook() : null;
    }
}
