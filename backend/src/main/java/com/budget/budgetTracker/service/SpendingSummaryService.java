package com.budget.budgetTracker.service;

import com.budget.budgetTracker.dto.SpendingSummaryDTO;
import com.budget.budgetTracker.entity.Transaction;
import com.budget.budgetTracker.entity.TransactionType;
import com.budget.budgetTracker.repository.TransactionRepository;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SpendingSummaryService {

    private final TransactionRepository txRepo;
    private final OpenAiChatModel chatClient;

    public SpendingSummaryService(TransactionRepository txRepo,
                                  OpenAiChatModel chatClient) {
        this.txRepo = txRepo;
        this.chatClient = chatClient;
    }

    public SpendingSummaryDTO summarize(Long userId, int month, int year) {
        YearMonth ym = YearMonth.of(year, month);

        List<Transaction> expenses = txRepo.findByUserIdAndType(userId, TransactionType.EXPENSE)
                .stream()
                .filter(t -> {
                    var d = t.getTimestamp();
                    return d.getYear() == year && d.getMonthValue() == month;
                })
                .toList();

        if (expenses.isEmpty()) {
            return new SpendingSummaryDTO("No expenses recorded for " + ym + ".");
        }

        // group by category
        Map<String, BigDecimal> byCat = expenses.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getCategory().name(),
                        Collectors.mapping(Transaction::getAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)))
                );

        BigDecimal total = expenses.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // build prompt
        StringBuilder prompt = new StringBuilder(
                "Write a 3-line friendly summary of this month's spending:\n");
        prompt.append("Total: ").append(total).append("\n");
        byCat.forEach((k, v) -> prompt.append(k).append(": ").append(v).append("\n"));

        var response = chatClient.call(new Prompt(prompt.toString()))
                .getResult()
                .getOutput()
                .getText();

        return new SpendingSummaryDTO(response.trim());
    }
}