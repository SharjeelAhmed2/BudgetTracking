package com.budget.budgetTracker.repository;

import com.budget.budgetTracker.entity.Budget;
import com.budget.budgetTracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Budget findByUserAndMonthAndYear(User user, int month, int year);
}