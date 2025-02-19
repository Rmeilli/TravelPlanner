package org.sid.travelplanner.controller;

import lombok.RequiredArgsConstructor;
import org.sid.travelplanner.model.Invitation;
import org.sid.travelplanner.service.InvitationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invitations")
public class InvitationController {
    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    private final InvitationService invitationService;

    @PostMapping
    public ResponseEntity<Invitation> createInvitation(
            @RequestParam Long tripId,
            @RequestParam String invitedEmail,
            @RequestParam Long inviterUserId) {
        return ResponseEntity.ok(invitationService.createInvitation(tripId, invitedEmail, inviterUserId));
    }

    @PostMapping("/{token}/respond")
    public ResponseEntity<Invitation> respondToInvitation(
            @PathVariable String token,
            @RequestParam boolean accept) {
        return ResponseEntity.ok(invitationService.respondToInvitation(token, accept));
    }
}