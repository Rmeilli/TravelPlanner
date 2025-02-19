package org.sid.travelplanner.service;

import org.sid.travelplanner.dto.BalanceDTO;
import org.sid.travelplanner.dto.ExpenseDTO;
import org.sid.travelplanner.model.*;
import org.sid.travelplanner.repository.ExpenseRepository;
import org.sid.travelplanner.repository.ExpenseShareRepository;
import org.sid.travelplanner.repository.TripRepository;
import org.sid.travelplanner.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ExpenseService {
    public ExpenseService(ExpenseRepository expenseRepository, ExpenseShareRepository shareRepository, TripRepository tripRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.shareRepository = shareRepository;
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
    }

    private final ExpenseRepository expenseRepository;
    private final ExpenseShareRepository shareRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    public Expense createExpense(Long tripId, Long paidById, ExpenseDTO expenseDTO) {
        // Vérifier si le voyage existe
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Voyage non trouvé"));

        // Vérifier si l'utilisateur qui a payé existe
        User paidBy = userRepository.findById(paidById)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Créer la dépense
        Expense expense = new Expense();
        expense.setTrip(trip);
        expense.setPaidBy(paidBy);
        expense.setDescription(expenseDTO.getDescription());
        expense.setAmount(expenseDTO.getAmount());
        expense.setDate(expenseDTO.getDate());
        expense.setCategory(expenseDTO.getCategory());

        expense = expenseRepository.save(expense);

        // Créer les parts pour chaque participant
        for (Map.Entry<Long, BigDecimal> share : expenseDTO.getShares().entrySet()) {
            User user = userRepository.findById(share.getKey())
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            ExpenseShare expenseShare = new ExpenseShare();
            expenseShare.setExpense(expense);
            expenseShare.setUser(user);
            expenseShare.setAmount(share.getValue());
            expenseShare.setPaid(false);
            shareRepository.save(expenseShare);
        }

        return expense;
    }

    public List<Expense> getTripExpenses(Long tripId) {
        // Vérifier si le voyage existe
        if (!tripRepository.existsById(tripId)) {
            throw new RuntimeException("Voyage non trouvé");
        }
        return expenseRepository.findByTripId(tripId);
    }


    public void markShareAsPaid(Long shareId) {
        ExpenseShare share = shareRepository.findById(shareId)
                .orElseThrow(() -> new RuntimeException("Part de dépense non trouvée"));
        share.setPaid(true);
        shareRepository.save(share);
    }

    public List<ExpenseShare> getUnpaidShares(Long tripId, Long userId) {
        return shareRepository.findByExpenseTripIdAndUserIdAndPaidFalse(tripId, userId);
    }

    public BigDecimal getTotalExpenses(Long tripId) {
        return expenseRepository.findByTripId(tripId).stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<ExpenseCategory, BigDecimal> getExpensesByCategory(Long tripId) {
        List<Expense> expenses = expenseRepository.findByTripId(tripId);
        Map<ExpenseCategory, BigDecimal> categoryTotals = new HashMap<>();

        for (Expense expense : expenses) {
            categoryTotals.merge(expense.getCategory(),
                    expense.getAmount(),
                    BigDecimal::add);
        }

        return categoryTotals;
    }

    public void deleteExpense(Long expenseId) {
        // Vérifier si la dépense existe
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Dépense non trouvée"));

        // Supprimer d'abord les parts associées
        shareRepository.deleteByExpenseId(expenseId);

        // Puis supprimer la dépense
        expenseRepository.delete(expense);
    }

    public List<BalanceDTO> calculateBalances(Long tripId) {
        List<ExpenseShare> shares = shareRepository.findByExpenseTripId(tripId);
        Map<Long, BalanceDTO> balances = new HashMap<>();

        // Initialiser les DTO pour chaque utilisateur
        for (ExpenseShare share : shares) {
            Long userId = share.getUser().getId();
            if (!balances.containsKey(userId)) {
                BalanceDTO dto = new BalanceDTO();
                dto.setUserId(userId);
                dto.setUserName(share.getUser().getName());
                dto.setTotalPaid(BigDecimal.ZERO);
                dto.setTotalDue(BigDecimal.ZERO);
                balances.put(userId, dto);
            }
        }

        // Calculer les totaux
        for (ExpenseShare share : shares) {
            Long userId = share.getUser().getId();
            Expense expense = share.getExpense();
            BalanceDTO balance = balances.get(userId);

            // Ajouter ce que la personne a payé
            if (expense.getPaidBy().getId().equals(userId)) {
                balance.setTotalPaid(balance.getTotalPaid().add(expense.getAmount()));
            }

            // Ajouter ce que la personne doit
            balance.setTotalDue(balance.getTotalDue().add(share.getAmount()));
        }

        // Calculer les bilans finaux
        for (BalanceDTO balance : balances.values()) {
            balance.setBalance(balance.getTotalPaid().subtract(balance.getTotalDue()));
        }

        return new ArrayList<>(balances.values());
    }
}
