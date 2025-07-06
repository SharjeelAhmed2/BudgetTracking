package com.budget.budgetTracker.controller;

import com.budget.budgetTracker.dto.BudgetOverviewDTO;
import com.budget.budgetTracker.entity.Budget;
import com.budget.budgetTracker.service.BudgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173/")
@RequestMapping("/budget")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Budget> createOrUpdateBudget(
            @PathVariable Long userId,
            @RequestBody Budget budgetInput
    ) {
        return ResponseEntity.ok(budgetService.createOrUpdateBudget(userId, budgetInput));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<BudgetOverviewDTO> getBudgetOverview(
            @PathVariable Long userId,
            @RequestParam int month,
            @RequestParam int year
    ) {
        return ResponseEntity.ok(budgetService.getBudgetOverview(userId, month, year));
    }
}