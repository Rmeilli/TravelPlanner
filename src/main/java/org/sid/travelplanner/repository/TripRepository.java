package org.sid.travelplanner.repository;

import org.sid.travelplanner.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByCreatorId(Long creatorId);
}