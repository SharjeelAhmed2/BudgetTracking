package com.budget.budgetTracker.dto;

import java.math.BigDecimal;

public class BudgetOverviewDTO {

    private BigDecimal limit;
    private BigDecimal totalSpent;
    private String status; // "EXCEEDED" or "OK"

    // Getters and setters

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}