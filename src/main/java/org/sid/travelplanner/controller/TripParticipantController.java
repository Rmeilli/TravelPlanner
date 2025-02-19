package org.sid.travelplanner.controller;

import lombok.RequiredArgsConstructor;
import org.sid.travelplanner.dto.TripInvitationDTO;
import org.sid.travelplanner.model.Trip;
import org.sid.travelplanner.model.TripParticipant;
import org.sid.travelplanner.service.TripParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trips")
public class TripParticipantController {
    private final TripParticipantService participantService;

    public TripParticipantController(TripParticipantService participantService) {
        this.participantService = participantService;
    }

    @PostMapping("/{tripId}/invite")
    public ResponseEntity<?> inviteParticipants(
            @PathVariable Long tripId,
            @RequestBody TripInvitationDTO invitation) {
        participantService.inviteParticipants(tripId, invitation);
        return ResponseEntity.ok(Map.of(
                "message", "Invitations envoyées avec succès",
                "invitedEmails", invitation.getEmails()
        ));
    }
    @PostMapping("/{tripId}/accept")
    public ResponseEntity<Void> acceptInvitation(
            @PathVariable Long tripId,
            @RequestParam Long userId) {
        participantService.acceptInvitation(tripId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{tripId}/decline")
    public ResponseEntity<Void> declineInvitation(
            @PathVariable Long tripId,
            @RequestParam Long userId) {
        participantService.declineInvitation(tripId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{tripId}/participants")
    public ResponseEntity<List<TripParticipant>> getTripParticipants(
            @PathVariable Long tripId) {
        return ResponseEntity.ok(participantService.getTripParticipants(tripId));
    }

    @GetMapping("/invitations")
    public ResponseEntity<?> getUserInvitations(@RequestParam Long userId) {
        try {
            List<Trip> invitations = participantService.getUserInvitations(userId);
            if (invitations.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                        "message", "Aucune invitation en attente",
                        "invitations", invitations
                ));
            }
            return ResponseEntity.ok(Map.of(
                    "message", "Invitations trouvées",
                    "invitations", invitations
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}