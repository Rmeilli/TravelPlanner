package org.sid.travelplanner.repository;

import org.sid.travelplanner.model.Trip;
import org.sid.travelplanner.model.TripParticipant;
import org.sid.travelplanner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TripParticipantRepository extends JpaRepository<TripParticipant, Long> {
    boolean existsByTripAndUser(Trip trip, User user);
    Optional<TripParticipant> findByTripIdAndUserId(Long tripId, Long userId);
    List<TripParticipant> findByTripId(Long tripId);

    @Query("SELECT tp FROM TripParticipant tp WHERE tp.user.id = :userId AND tp.accepted = false")
    List<TripParticipant> findPendingInvitationsByUserId(Long userId);
}
