package com.tcs.Library.scheduler;

import com.tcs.Library.entity.IssuedBooks;
import com.tcs.Library.entity.User;
import com.tcs.Library.enums.IssueStatus;
import com.tcs.Library.repository.IssuedBooksRepo;
import com.tcs.Library.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaulterScheduler {

    private final IssuedBooksRepo issuedBooksRepo;
    private final UserRepo userRepo;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updateDefaulterStatuses() {
        log.info("Starting daily defaulter status update job");

        LocalDate today = LocalDate.now();

        // 1. Mark overdue books status
        List<IssuedBooks> overdueBooks = issuedBooksRepo.findOverdueBooks(today);
        for (IssuedBooks book : overdueBooks) {
            if (!IssueStatus.OVERDUE.equals(book.getStatus())) {
                book.setStatus(IssueStatus.OVERDUE);
                issuedBooksRepo.save(book);
            }
        }

        // 2. Identify Defaulters (Any unpaid fine > 0 OR Any overdue book)
        Set<Long> defaulterUserIds = new HashSet<>();

        // A. Users with unpaid fines
        List<User> usersWithFines = userRepo.findByTotalUnpaidFineGreaterThan(BigDecimal.ZERO);
        for (User user : usersWithFines) {
            defaulterUserIds.add(user.getId());
        }

        // B. Users with overdue books (findOverdueBooks returns IssuedBooks, we can get
        // users from there)
        // Note: findOverdueBooks returns books that ARE overdue. We want all books that
        // have status OVERDUE.
        // The above loop just updated them. So we can re-query or just iterate.
        // Let's query all active overdue books to be safe and cover all cases
        List<IssuedBooks> allOverdueBooks = issuedBooksRepo.findByStatus(IssueStatus.OVERDUE);
        for (IssuedBooks book : allOverdueBooks) {
            defaulterUserIds.add(book.getUser().getId());
        }

        // 3. Update User Statuses
        List<User> allUsers = userRepo.findAll();
        int newDefaulters = 0;
        int removedDefaulters = 0;

        for (User user : allUsers) {
            boolean shouldBeDefaulter = defaulterUserIds.contains(user.getId());

            if (user.isDefaulter() != shouldBeDefaulter) {
                user.setDefaulter(shouldBeDefaulter);
                userRepo.save(user);

                if (shouldBeDefaulter) {
                    newDefaulters++;
                    log.warn("User {} marked as defaulter", user.getEmail());
                } else {
                    removedDefaulters++;
                    log.info("User {} removed from defaulter list", user.getEmail());
                }
            }
        }

        log.info("Defaulter status update job completed. {} new defaulters, {} removed.", newDefaulters,
                removedDefaulters);
    }
}
