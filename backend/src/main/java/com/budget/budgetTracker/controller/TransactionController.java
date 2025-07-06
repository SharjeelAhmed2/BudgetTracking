package com.budget.budgetTracker.controller;

import com.budget.budgetTracker.dto.TransactionRequestDTO;
import com.budget.budgetTracker.entity.Transaction;
import com.budget.budgetTracker.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173/")
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Transaction> createTransaction(
            @RequestBody @Valid TransactionRequestDTO requestDTO,
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(transactionService.createTransaction(requestDTO, userId));
    }

//    @GetMapping("/{userId}")
//    public ResponseEntity<List<Transaction>> getTransactionsByUser(@PathVariable Long userId) {
//        return ResponseEntity.ok(transactionService.getTransactionsByUser(userId));
//    }
    @GetMapping("/{userId}")
    public ResponseEntity<List<TransactionRequestDTO>> getTransactionsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(transactionService.getTransactionsByUser(userId));
    }
    @GetMapping("/totalSpent/{userId}")
    public ResponseEntity<BigDecimal> getTotalAmount(@PathVariable long userId)
    {
        return ResponseEntity.ok(transactionService.getTransactionTotal(userId));
    }
}