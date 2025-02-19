package org.sid.travelplanner.repository;

import org.sid.travelplanner.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByTripId(Long tripId);
    List<Activity> findByTripIdAndStartDateTimeBetween(
            Long tripId, LocalDateTime start, LocalDateTime end);
}