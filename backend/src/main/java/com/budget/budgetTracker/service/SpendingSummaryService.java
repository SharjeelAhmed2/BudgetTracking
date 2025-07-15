package com.budget.budgetTracker.service;

import com.budget.budgetTracker.dto.AdvisorDTO;
import com.budget.budgetTracker.dto.SpendingSummaryDTO;
import com.budget.budgetTracker.entity.GPTAdvisor;
import com.budget.budgetTracker.entity.OpenAISummary;
import com.budget.budgetTracker.entity.Transaction;
import com.budget.budgetTracker.entity.TransactionType;
import com.budget.budgetTracker.repository.AdvisorRepository;
import com.budget.budgetTracker.repository.OpenAISummaryRepository;
import com.budget.budgetTracker.repository.TransactionRepository;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SpendingSummaryService {

    private final TransactionRepository txRepo;
    private final OpenAiChatModel chatClient;

    private final OpenAISummaryRepository summaryRepo;

    private final AdvisorRepository advisorRepository;
    public SpendingSummaryService(TransactionRepository txRepo,
                                  OpenAiChatModel chatClient, OpenAISummaryRepository summaryRepo, AdvisorRepository advisorRepository) {
        this.txRepo = txRepo;
        this.chatClient = chatClient;
        this.summaryRepo = summaryRepo;
        this.advisorRepository = advisorRepository;
    }

    public SpendingSummaryDTO summarize(Long userId, int month, int year) {
        var cached = summaryRepo.findTopByUserIdAndMonthAndYearOrderByIdDesc(userId, month, year);
        if (cached.isPresent() && !cached.get().isStale()) {
            return new SpendingSummaryDTO(cached.get().getSummary());
        }

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
                        Transaction::getCategory,
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

        String generatedSummary = chatClient.call(new Prompt(prompt.toString()))
                .getResult()
                .getOutput()
                .getText();

        // Save to DB
        OpenAISummary aiSummary = new OpenAISummary();
        aiSummary.setUserId(userId);
        aiSummary.setSummary(generatedSummary.trim());
        aiSummary.setMonth(month);
        aiSummary.setYear(year);
        aiSummary.setCreatedAt(LocalDateTime.now());

        summaryRepo.save(aiSummary);


        return new SpendingSummaryDTO(generatedSummary.trim());
    }
    public void markSummaryStale(Long userId, int month, int year) {
        summaryRepo.findTopByUserIdAndMonthAndYearOrderByIdDesc(userId, month, year)
                .ifPresent(s -> {
                    s.setStale(true);
                    summaryRepo.save(s);
                });
    }

    public AdvisorDTO suggestBudget(Long userId, int month, int year) {
        var cached = advisorRepository.findTopByUserIdAndMonthAndYearOrderByIdDesc(userId, month, year);
        if (cached.isPresent() && !cached.get().isStale()) {
            return new AdvisorDTO(cached.get().getAdvice());
        }

        YearMonth ym = YearMonth.of(year, month);

        List<Transaction> expenses = txRepo.findByUserIdAndType(userId, TransactionType.EXPENSE)
                .stream()
                .filter(t -> {
                    var d = t.getTimestamp();
                    return d.getYear() == year && d.getMonthValue() == month;
                })
                .toList();

        if (expenses.isEmpty()) {
            return new AdvisorDTO("No expenses recorded for " + ym + ".");
        }

        // group by category
        Map<String, BigDecimal> byCat = expenses.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.mapping(Transaction::getAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)))
                );

        BigDecimal total = expenses.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // build prompt
        String prompt = "You are a financial advisor.\n"
                + "User's total spending this month: " + total + "\n"
                + "Category breakdown:\n"
                + byCat.entrySet().stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining("\\n"))
                + "\nGive three concrete tips to keep next-month spending within budget, ranking them by categories"
                + "Respond in 3 concise bullet points.";

        String generatedAdvice = chatClient.call(new Prompt(prompt))
                .getResult()
                .getOutput()
                .getText();

        // Save to DB
        GPTAdvisor gptAdvisor = new GPTAdvisor();
        gptAdvisor.setUserId(userId);
        gptAdvisor.setAdvice(generatedAdvice.trim());
        gptAdvisor.setMonth(month);
        gptAdvisor.setYear(year);
        gptAdvisor.setCreatedAt(LocalDateTime.now());

        advisorRepository.save(gptAdvisor);


        return new AdvisorDTO(generatedAdvice.trim());
    }
    public void makeAdivceState(Long userId, int month, int year) {
        advisorRepository.findTopByUserIdAndMonthAndYearOrderByIdDesc(userId, month, year)
                .ifPresent(s -> {
                    s.setStale(true);
                    advisorRepository.save(s);
                });
    }
}