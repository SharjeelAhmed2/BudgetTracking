package com.budget.budgetTracker.service;

import com.budget.budgetTracker.dto.TransactionRequestDTO;
import com.budget.budgetTracker.entity.*;
import com.budget.budgetTracker.repository.BudgetRepository;
import com.budget.budgetTracker.repository.OpenAISummaryRepository;
import com.budget.budgetTracker.repository.TransactionRepository;
import com.budget.budgetTracker.repository.UserRepository;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final OpenAiChatModel chatClient;

    private final OpenAISummaryRepository summaryRepo;
    private final TransactionRepository transactionRepo;

    private final BudgetRepository budgetRepo;
    private final UserRepository userRepo;

    private final EmailService emailService;

    private final SpendingSummaryService summaryService;
    public TransactionService(OpenAiChatModel chatClient, OpenAISummaryRepository summaryRepo, TransactionRepository transactionRepo, UserRepository userRepo, BudgetRepository budgetRepo, EmailService emailService, SpendingSummaryService summaryService) {
        this.chatClient = chatClient;
        this.summaryRepo = summaryRepo;
        this.transactionRepo = transactionRepo;
        this.userRepo = userRepo;
        this.budgetRepo = budgetRepo;
        this.emailService = emailService;
        this.summaryService = summaryService;
    }

    public Transaction createTransaction(TransactionRequestDTO requestDTO, Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String categories = Arrays.stream(CategoryType.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));

        Transaction transaction = new Transaction();
        transaction.setTitle(requestDTO.getTitle());
        transaction.setAmount(requestDTO.getAmount());
        transaction.setType(requestDTO.getType());
        StringBuilder prompt = new StringBuilder("You are an intelligent categorization model.\n");
        prompt.append("Your task is to determine which category the following title best fits into.\n");
        prompt.append("Choose *only one* from these categories: ").append(categories).append(".\n");
        prompt.append("Title: ").append(requestDTO.getTitle()).append("\n");
        prompt.append("Return only the category name (e.g., TRAVEL). No explanation.");


        String generatedSummary = chatClient.call(new Prompt(prompt.toString()))
                .getResult()
                .getOutput()
                .getText();
        System.out.println("ChatGPT response: " + generatedSummary);
        transaction.setCategory(generatedSummary);
        transaction.setUser(user);

        if(user.getTotalBalance() != null) {
            // Make Changes to Total Balance
            BigDecimal transactionAmount = transaction.getAmount();
            BigDecimal totalBalance = user.getTotalBalance();
            BigDecimal totalAfterTransaction = BigDecimal.valueOf(0);
            if (transaction.getType() == TransactionType.INCOME) {
                totalAfterTransaction = totalBalance.add(transactionAmount);
            } else if (transaction.getType() == TransactionType.EXPENSE) {
                totalAfterTransaction = totalBalance.subtract(transactionAmount);
            }
            user.setTotalBalance(totalAfterTransaction);
            userRepo.save(user);
        }
        // Save Transaction
        Transaction savedTransaction = transactionRepo.save(transaction);

        LocalDateTime now = LocalDateTime.now();
        int month = now.getMonthValue();
        int year = now.getYear();
        summaryService.markSummaryStale(userId, month,year);
        summaryService.suggestBudget(userId, month,year);
        // Budget Exceed Alert Send
        Budget budget = budgetRepo.findByUserAndMonthAndYear(user, month, year);
        if (budget != null && !budget.isAlertSent()) {
            BigDecimal totalSpent = transactionRepo.findByUser(user).stream()
                    .filter(t -> t.getType() == TransactionType.EXPENSE)
                    .filter(t -> {
                        LocalDateTime tDate = t.getTimestamp();
                        return tDate.getMonthValue() == month && tDate.getYear() == year;
                    })
                    .map(Transaction::getAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (totalSpent.compareTo(budget.getLimitAmount()) > 0) {
                emailService.sendBudgetExceededAlert(
                        user.getEmail(), user.getName(), budget.getLimitAmount(), totalSpent
                );
                budget.setAlertSent(true);
                budgetRepo.save(budget);
            }
        }
        return savedTransaction;
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

    public BigDecimal getTransactionTotal(Long userId)
    {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        LocalDateTime now = LocalDateTime.now();
        int month = now.getMonthValue();
        int year = now.getYear();

        return transactionRepo.findByUser(user).stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .filter(t -> {
                    LocalDateTime tDate = t.getTimestamp();
                    return tDate.getMonthValue() == month && tDate.getYear() == year;
                })
                .map(Transaction::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}