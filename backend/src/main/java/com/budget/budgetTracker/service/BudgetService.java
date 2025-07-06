package com.budget.budgetTracker.service;


import com.budget.budgetTracker.dto.BudgetOverviewDTO;
import com.budget.budgetTracker.entity.Budget;
import com.budget.budgetTracker.entity.TransactionType;
import com.budget.budgetTracker.entity.User;
import com.budget.budgetTracker.repository.BudgetRepository;
import com.budget.budgetTracker.repository.TransactionRepository;
import com.budget.budgetTracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.budget.budgetTracker.entity.Transaction;
import java.math.BigDecimal;
import java.util.Objects;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepo;
    private final UserRepository userRepo;
    private final TransactionRepository transactionRepo;

    private final EmailService emailService;

    public BudgetService(BudgetRepository budgetRepo, UserRepository userRepo, TransactionRepository transactionRepo, EmailService emailService) {
        this.budgetRepo = budgetRepo;
        this.userRepo = userRepo;
        this.transactionRepo = transactionRepo;
        this.emailService = emailService;
    }

    public Budget createOrUpdateBudget(Long userId, Budget budgetInput) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Budget existing = budgetRepo.findByUserAndMonthAndYear(user, budgetInput.getMonth(), budgetInput.getYear());

        if (existing != null) {
            existing.setLimitAmount(budgetInput.getLimitAmount());
            // Fetch current total spent for that month
            BigDecimal totalSpent = transactionRepo.findByUser(user).stream()
                    .filter(t -> t.getType() == TransactionType.EXPENSE)
                    .filter(t -> t.getTimestamp().getMonthValue() == budgetInput.getMonth()
                            && t.getTimestamp().getYear() == budgetInput.getYear())
                    .map(Transaction::getAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // If new budget is greater than total spent, reset alert and notify
            if (totalSpent.compareTo(budgetInput.getLimitAmount()) < 0 && existing.isAlertSent()) {
                existing.setAlertSent(false);
                emailService.sendBudgetUnderControl(user.getEmail(), user.getName(), budgetInput.getLimitAmount(), totalSpent);
            }
            return budgetRepo.save(existing);
        }
        budgetInput.setUser(user);
        return budgetRepo.save(budgetInput);
    }

    public BudgetOverviewDTO getBudgetOverview(Long userId, int month, int year) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Budget budget = budgetRepo.findByUserAndMonthAndYear(user, month, year);
        if (budget == null) {
            throw new RuntimeException("Budget not set for this month");
        }

        BigDecimal totalSpent = transactionRepo.findByUser(user).stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .filter(t -> t.getTimestamp().getMonthValue() == month && t.getTimestamp().getYear() == year)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BudgetOverviewDTO dto = new BudgetOverviewDTO();
        dto.setLimit(budget.getLimitAmount());
       // dto.setTotalSpent(totalSpent);
        dto.setStatus(totalSpent.compareTo(budget.getLimitAmount()) > 0 ? "EXCEEDED" : "OK");

        return dto;
    }
}