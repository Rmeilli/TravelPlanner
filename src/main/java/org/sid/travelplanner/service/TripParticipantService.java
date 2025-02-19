package org.sid.travelplanner.service;

import lombok.RequiredArgsConstructor;
import org.sid.travelplanner.dto.TripInvitationDTO;
import org.sid.travelplanner.model.Trip;
import org.sid.travelplanner.model.TripParticipant;
import org.sid.travelplanner.model.TripRole;
import org.sid.travelplanner.model.User;
import org.sid.travelplanner.repository.TripParticipantRepository;
import org.sid.travelplanner.repository.TripRepository;
import org.sid.travelplanner.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TripParticipantService {
    public TripParticipantService(TripRepository tripRepository, UserRepository userRepository, TripParticipantRepository participantRepository) {
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
        this.participantRepository = participantRepository;
    }

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final TripParticipantRepository participantRepository;

    public void inviteParticipants(Long tripId, TripInvitationDTO invitation) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Voyage non trouvé"));

        for (String email : invitation.getEmails()) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé: " + email));

            // Vérifier si l'utilisateur n'est pas déjà invité
            if (participantRepository.existsByTripAndUser(trip, user)) {
                continue;
            }

            TripParticipant participant = new TripParticipant();
            participant.setTrip(trip);
            participant.setUser(user);
            participant.setRole(TripRole.PARTICIPANT);
            participant.setInvitedAt(LocalDateTime.now());

            participantRepository.save(participant);
            // TODO: Envoyer un email d'invitation
        }
    }

    public void acceptInvitation(Long tripId, Long userId) {
        TripParticipant participant = participantRepository.findByTripIdAndUserId(tripId, userId)
                .orElseThrow(() -> new RuntimeException("Invitation non trouvée"));

        participant.setAccepted(true);
        participant.setAcceptedAt(LocalDateTime.now());
        participantRepository.save(participant);
    }

    public void declineInvitation(Long tripId, Long userId) {
        TripParticipant participant = participantRepository.findByTripIdAndUserId(tripId, userId)
                .orElseThrow(() -> new RuntimeException("Invitation non trouvée"));

        participantRepository.delete(participant);
    }

    public List<TripParticipant> getTripParticipants(Long tripId) {
        return participantRepository.findByTripId(tripId);
    }

    public List<Trip> getUserInvitations(Long userId) {
        // Vérifier d'abord si l'utilisateur existe
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Utilisateur non trouvé avec l'ID : " + userId);
        }

        List<TripParticipant> pendingInvitations = participantRepository.findPendingInvitationsByUserId(userId);

        // Log pour le débogage
        System.out.println("Nombre d'invitations trouvées pour l'utilisateur " + userId + ": " + pendingInvitations.size());

        return pendingInvitations.stream()
                .map(TripParticipant::getTrip)
                .collect(Collectors.toList());
    }
}
