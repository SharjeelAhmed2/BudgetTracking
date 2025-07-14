package com.budget.budgetTracker.dto;

import lombok.Data;

@Data
public class AdvisorDTO {
    public AdvisorDTO(String summary) {
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
