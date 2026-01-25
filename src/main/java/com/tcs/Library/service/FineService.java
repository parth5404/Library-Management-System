package com.tcs.Library.service;

import com.tcs.Library.entity.Fine;
import com.tcs.Library.entity.User;
import com.tcs.Library.enums.IssueStatus;
import com.tcs.Library.repository.FineRepo;
import com.tcs.Library.repository.IssuedBooksRepo;
import com.tcs.Library.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FineService {

    private final FineRepo fineRepo;
    private final UserRepo userRepo;
    private final IssuedBooksRepo issuedBooksRepo;

    public List<Fine> getUserFines(Long userId) {
        return fineRepo.findByUserId(userId);
    }

    public List<Fine> getUnpaidFines(Long userId) {
        return fineRepo.findByUserIdAndIsPaid(userId, false);
    }

    @Transactional
    public Fine payFine(Long fineId) {
        Fine fine = fineRepo.findById(fineId)
                .orElseThrow(() -> new RuntimeException("Fine not found: " + fineId));

        if (fine.isPaid()) {
            throw new RuntimeException("Fine is already paid");
        }

        fine.setPaid(true);
        fine.setPaidAt(LocalDate.now());
        fineRepo.save(fine);

        // Update user's total unpaid fine
        User user = fine.getUser();
        BigDecimal newTotal = fineRepo.getTotalUnpaidFineByUser(user.getId());
        user.setTotalUnpaidFine(newTotal);

        // Recalculate defaulter status
        recalculateDefaulterStatus(user);
        userRepo.save(user);

        log.info("Fine {} paid by user {}", fineId, user.getEmail());
        return fine;
    }

    public void recalculateDefaulterStatus(User user) {
        boolean shouldBeDefaulter = false;

        // 1. Check unpaid fines (Any amount > 0)
        if (user.getTotalUnpaidFine().compareTo(BigDecimal.ZERO) > 0) {
            shouldBeDefaulter = true;
        }

        // 2. Check overdue books (Any book with status OVERDUE)
        if (!shouldBeDefaulter) {
            boolean hasOverdueBooks = issuedBooksRepo.countByUserIdAndStatus(user.getId(), IssueStatus.OVERDUE) > 0;
            if (hasOverdueBooks) {
                shouldBeDefaulter = true;
            }
        }

        // Only save if status changed to avoid unnecessary DB writes
        if (user.isDefaulter() != shouldBeDefaulter) {
            user.setDefaulter(shouldBeDefaulter);
            if (shouldBeDefaulter) {
                log.warn("User {} marked as defaulter", user.getEmail());
            } else {
                log.info("User {} removed from defaulter list", user.getEmail());
            }
            // Note: The caller (payFine) saves the user, but if called independently we
            // might need to save.
            // However, looking at usage, payFine calls userRepo.save(user) AFTER this.
            // But to be safe and consistent with previous logic which didn't save inside
            // this method but relied on caller:
            // The previous code SET the value but didn't SAVE it in this method (it was
            // saved in payFine).
            // BUT wait, previous code for 'payFine' does: recalculate -> userRepo.save.
            // Let's keep it that way. This method just updates the entity state.
        }
    }
}
