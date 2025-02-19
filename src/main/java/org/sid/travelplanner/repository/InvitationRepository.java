package org.sid.travelplanner.repository;

import org.sid.travelplanner.model.Invitation;
import org.sid.travelplanner.model.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    Optional<Invitation> findByToken(String token);
    List<Invitation> findByTripIdAndStatus(Long tripId, InvitationStatus status);
    List<Invitation> findByInvitedEmailAndStatus(String email, InvitationStatus status);
    boolean existsByTripIdAndInvitedEmailAndStatus(Long tripId, String email, InvitationStatus status);
}

