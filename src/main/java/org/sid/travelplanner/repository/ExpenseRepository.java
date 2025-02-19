package org.sid.travelplanner.repository;

import org.sid.travelplanner.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByTripId(Long tripId);
}
