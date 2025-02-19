package org.sid.travelplanner.service;

import lombok.RequiredArgsConstructor;
import org.sid.travelplanner.exception.ResourceNotFoundException;
import org.sid.travelplanner.model.*;
import org.sid.travelplanner.repository.NotificationRepository;
import org.sid.travelplanner.repository.TripRepository;
import org.sid.travelplanner.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import java.util.List;


@Service
@Transactional
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository, TripRepository tripRepository, EmailService emailService) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
        this.emailService = emailService;
    }

    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final EmailService emailService;

    private void createAndSaveNotification(Trip trip, User user, NotificationType type,
                                           String title, String message, String link) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTrip(trip);
        notification.setType(type);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setLink(link);
        notificationRepository.save(notification);
    }

    public void notifyNewActivity(Long tripId, Long activityId, String activityName,
                                  String activityDescription, String activityDate) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Voyage non trouvé"));

        String baseUrl = "http://localhost:8080"; // À configurer selon l'environnement
        String link = baseUrl + "/trips/" + tripId + "/activities/" + activityId;

        for (TripParticipant participant : trip.getParticipants()) {
            User user = participant.getUser();

            // Créer la notification en base
            Notification notification = new Notification();
            notification.setUser(user);
            notification.setTrip(trip);
            notification.setType(NotificationType.NEW_ACTIVITY);
            notification.setTitle("Nouvelle activité proposée");
            notification.setMessage("Une nouvelle activité '" + activityName + "' a été proposée.");
            notification.setLink(link);
            notificationRepository.save(notification);

            // Envoyer l'email
            Context context = new Context();
            context.setVariable("userName", user.getName());
            context.setVariable("destination", trip.getDestination());
            context.setVariable("activityName", activityName);
            context.setVariable("activityDescription", activityDescription);
            context.setVariable("activityDate", activityDate);
            context.setVariable("link", link);

            emailService.sendNotificationEmail(
                    user.getEmail(),
                    "Nouvelle activité proposée pour votre voyage",
                    "email/new-activity",
                    context
            );
        }
    }

    public void notifyNewVote(Long tripId, Long activityId, Long voterId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Voyage non trouvé"));

        User voter = userRepository.findById(voterId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        String baseUrl = "http://localhost:8080";
        String link = baseUrl + "/trips/" + tripId + "/activities/" + activityId;

        for (TripParticipant participant : trip.getParticipants()) {
            User user = participant.getUser();
            if (!user.getId().equals(voterId)) {
                // Créer la notification en base
                Notification notification = new Notification();
                notification.setUser(user);
                notification.setTrip(trip);
                notification.setType(NotificationType.NEW_VOTE);
                notification.setTitle("Nouveau vote sur une activité");
                notification.setMessage(voter.getName() + " a voté sur une activité.");
                notification.setLink(link);
                notificationRepository.save(notification);

                // Envoyer l'email
                Context context = new Context();
                context.setVariable("userName", user.getName());
                context.setVariable("voterName", voter.getName());
                context.setVariable("activityName", ""); // À compléter avec le nom de l'activité
                context.setVariable("link", link);

                emailService.sendNotificationEmail(
                        user.getEmail(),
                        "Nouveau vote sur une activité",
                        "email/new-vote",
                        context
                );
            }
        }
    }

    public List<Notification> getUserNotifications(Long userId) {
        verifyUser(userId);
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Notification> getUnreadNotifications(Long userId) {
        verifyUser(userId);
        return notificationRepository.findByUserIdAndReadOrderByCreatedAtDesc(userId, false);
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification non trouvée"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public void markAllAsRead(Long userId) {
        verifyUser(userId);
        List<Notification> unreadNotifications = getUnreadNotifications(userId);
        unreadNotifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(unreadNotifications);
    }

    public long getUnreadCount(Long userId) {
        verifyUser(userId);
        return notificationRepository.countByUserIdAndRead(userId, false);
    }

    private void verifyUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Utilisateur non trouvé");
        }
    }
}