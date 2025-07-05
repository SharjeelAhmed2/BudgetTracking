package com.budget.budgetTracker.service;

import com.budget.budgetTracker.dto.TransactionRequestDTO;
import com.budget.budgetTracker.entity.Transaction;
import com.budget.budgetTracker.entity.User;
import com.budget.budgetTracker.repository.TransactionRepository;
import com.budget.budgetTracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepo;
    private final UserRepository userRepo;

    public TransactionService(TransactionRepository transactionRepo, UserRepository userRepo) {
        this.transactionRepo = transactionRepo;
        this.userRepo = userRepo;
    }

    public Transaction createTransaction(TransactionRequestDTO requestDTO, Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Transaction transaction = new Transaction();
        transaction.setTitle(requestDTO.getTitle());
        transaction.setAmount(requestDTO.getAmount());
        transaction.setType(requestDTO.getType());
        transaction.setCategory(requestDTO.getCategory());
        transaction.setUser(user);

        return transactionRepo.save(transaction);
    }

    public List<TransactionRequestDTO> getTransactionsByUser(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Transaction> transactions = transactionRepo.findByUser(user);

        return transactions.stream()
                .map(t -> {
                    TransactionRequestDTO dto = new TransactionRequestDTO();
                    dto.setTitle(t.getTitle());
                    dto.setAmount(t.getAmount());
                    dto.setType(t.getType());
                    dto.setCategory(t.getCategory());
                    return dto;
                })
                .toList();
    }
}