package com.budget.budgetTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data

public class SpendingSummaryDTO {
    public SpendingSummaryDTO(String summary) {
        this.summary = summary;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    private String summary;     // the AI-generated paragraph
}