package org.sid.travelplanner.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BalanceDTO {
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(BigDecimal totalPaid) {
        this.totalPaid = totalPaid;
    }

    public BigDecimal getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(BigDecimal totalDue) {
        this.totalDue = totalDue;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    private Long userId;
    private String userName;
    private BigDecimal totalPaid;      // Ce que la personne a payé
    private BigDecimal totalDue;       // Ce que la personne doit
    private BigDecimal balance;        // Bilan final (positif = doit recevoir, négatif = doit payer)
}

