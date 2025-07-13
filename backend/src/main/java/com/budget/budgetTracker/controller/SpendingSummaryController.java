package com.budget.budgetTracker.controller;

import com.budget.budgetTracker.dto.SpendingSummaryDTO;
import com.budget.budgetTracker.service.SpendingSummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/summary")
public class SpendingSummaryController {

    private final SpendingSummaryService summaryService;

    public SpendingSummaryController(SpendingSummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<SpendingSummaryDTO> getSummary(
            @PathVariable Long userId,
            @RequestParam int month,
            @RequestParam int year) {

        return ResponseEntity.ok(summaryService.summarize(userId, month, year));
    }
}