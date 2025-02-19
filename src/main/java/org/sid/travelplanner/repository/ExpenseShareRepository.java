package org.sid.travelplanner.repository;

import org.sid.travelplanner.model.ExpenseShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExpenseShareRepository extends JpaRepository<ExpenseShare, Long> {
    List<ExpenseShare> findByExpenseTripId(Long tripId);
    void deleteByExpenseId(Long expenseId);
    @Query("SELECT es FROM ExpenseShare es WHERE es.expense.trip.id = ?1 AND es.user.id = ?2 AND es.isPaid = false")
    List<ExpenseShare> findByExpenseTripIdAndUserIdAndPaidFalse(Long tripId, Long userId);
}
