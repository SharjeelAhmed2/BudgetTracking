package com.budget.budgetTracker.repository;

import com.budget.budgetTracker.entity.OpenAISummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OpenAISummaryRepository extends JpaRepository<OpenAISummary, Long> {
    Optional<OpenAISummary> findTopByUserIdAndMonthAndYearOrderByIdDesc(Long userId, int month, int year);
}