package org.sid.travelplanner.dto;


import lombok.Data;
import org.sid.travelplanner.model.ExpenseCategory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ExpenseDTO {
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    public Map<Long, BigDecimal> getShares() {
        return shares;
    }

    public void setShares(Map<Long, BigDecimal> shares) {
        this.shares = shares;
    }

    private String description;
    private BigDecimal amount;
    private LocalDateTime date;
    private ExpenseCategory category;
    private Map<Long, BigDecimal> shares; // userId -> montant
}