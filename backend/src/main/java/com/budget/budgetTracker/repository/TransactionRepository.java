package com.budget.budgetTracker.repository;

import com.budget.budgetTracker.dto.TransactionRequestDTO;
import com.budget.budgetTracker.entity.Transaction;
import com.budget.budgetTracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
}