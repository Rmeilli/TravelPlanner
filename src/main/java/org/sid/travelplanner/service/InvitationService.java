package org.sid.travelplanner.service;

// src/main/java/com/example/travelplanner/service/InvitationService.java

import org.sid.travelplanner.model.*;
import org.sid.travelplanner.repository.InvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class InvitationService {
    public InvitationService(InvitationRepository invitationRepository, TripService tripService, UserService userService, EmailService emailService) {
        this.invitationRepository = invitationRepository;
        this.tripService = tripService;
        this.userService = userService;
        this.emailService = emailService;
    }

    private final InvitationRepository invitationRepository;
    private final TripService tripService;
    private final UserService userService;
    private final EmailService emailService;

    public Invitation createInvitation(Long tripId, String invitedEmail, Long inviterUserId) {
        // Vérifier si une invitation en attente existe déjà
        if (invitationRepository.existsByTripIdAndInvitedEmailAndStatus(tripId, invitedEmail, InvitationStatus.PENDING)) {
            throw new RuntimeException("Une invitation est déjà en attente pour cet email");
        }

        Trip trip = tripService.getTripById(tripId);
        User inviter = userService.getUserById(inviterUserId);

        Invitation invitation = new Invitation();
        invitation.setTrip(trip);
        invitation.setInvitedEmail(invitedEmail);
        invitation.setInviter(inviter);
        invitation.setToken(generateToken());
        invitation.setExpiresAt(LocalDateTime.now().plusDays(7)); // L'invitation expire après 7 jours

        invitation = invitationRepository.save(invitation);

        // Envoyer l'email d'invitation
        sendInvitationEmail(invitation);

        return invitation;
    }

    public Invitation respondToInvitation(String token, boolean accept) {
        Invitation invitation = invitationRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invitation non trouvée"));

        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new RuntimeException("Cette invitation a déjà reçu une réponse");
        }

        if (invitation.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cette invitation a expiré");
        }

        invitation.setStatus(accept ? InvitationStatus.ACCEPTED : InvitationStatus.DECLINED);
        invitation.setRespondedAt(LocalDateTime.now());

        if (accept) {
            // Ajouter l'utilisateur comme participant au voyage
            tripService.addParticipant(invitation.getTrip().getId(), invitation.getInvitedEmail());
        }

        return invitationRepository.save(invitation);
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    private void sendInvitationEmail(Invitation invitation) {
        String invitationLink = "http://votre-site.com/invitation/" + invitation.getToken();

        emailService.sendNotificationEmail(
                invitation.getInvitedEmail(),
                "Invitation à rejoindre un voyage",
                "email/trip-invitation",
                createInvitationContext(invitation, invitationLink)
        );
    }
    private Context createInvitationContext(Invitation invitation, String invitationLink) {
        Context context = new Context();
        context.setVariable("inviterName", invitation.getInviter().getName());
        context.setVariable("tripName", invitation.getTrip().getDestination());
        context.setVariable("tripDates", formatTripDates(invitation.getTrip()));
        context.setVariable("invitationLink", invitationLink);
        context.setVariable("expirationDate", formatDate(invitation.getExpiresAt()));
        return context;
    }

    private String formatTripDates(Trip trip) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("du %s au %s",
                trip.getStartDate().format(formatter),
                trip.getEndDate().format(formatter));
    }

    private String formatDate(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    // Autres méthodes utiles...
}
