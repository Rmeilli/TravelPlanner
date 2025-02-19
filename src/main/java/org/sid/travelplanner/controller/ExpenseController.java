package org.sid.travelplanner.controller;

import org.sid.travelplanner.dto.BalanceDTO;
import org.sid.travelplanner.dto.ExpenseDTO;
import org.sid.travelplanner.model.Expense;
import org.sid.travelplanner.service.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trips/{tripId}/expenses")

public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<Expense> createExpense(
            @PathVariable Long tripId,
            @RequestParam Long paidById,
            @RequestBody ExpenseDTO expenseDTO) {
        return ResponseEntity.ok(expenseService.createExpense(tripId, paidById, expenseDTO));
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getTripExpenses(@PathVariable Long tripId) {
        return ResponseEntity.ok(expenseService.getTripExpenses(tripId));
    }


    @PostMapping("/shares/{shareId}/paid")
    public ResponseEntity<Void> markShareAsPaid(
            @PathVariable Long tripId,
            @PathVariable Long shareId) {
        expenseService.markShareAsPaid(shareId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/balances")
    public ResponseEntity<List<BalanceDTO>> getBalances(@PathVariable Long tripId) {
        return ResponseEntity.ok(expenseService.calculateBalances(tripId));
    }
}