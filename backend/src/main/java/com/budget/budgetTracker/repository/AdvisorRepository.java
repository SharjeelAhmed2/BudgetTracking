package com.budget.budgetTracker.repository;

import com.budget.budgetTracker.entity.GPTAdvisor;
import com.budget.budgetTracker.entity.OpenAISummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdvisorRepository extends JpaRepository<GPTAdvisor, Long> {
    Optional<GPTAdvisor> findTopByUserIdAndMonthAndYearOrderByIdDesc(Long userId, int month, int year);
}