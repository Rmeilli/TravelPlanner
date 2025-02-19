package org.sid.travelplanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sid.travelplanner.model.*;
import org.sid.travelplanner.repository.NotificationRepository;
import org.sid.travelplanner.repository.TripRepository;
import org.sid.travelplanner.repository.UserRepository;
import org.sid.travelplanner.service.EmailService;
import org.sid.travelplanner.service.NotificationService;
import org.springframework.boot.test.context.SpringBootTest;

import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTests {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificationService notificationService;

    private User testUser;
    private Trip testTrip;
    private TripParticipant testParticipant;

    @BeforeEach
    void setUp() {
        // Créer des données de test
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");

        testTrip = new Trip();
        testTrip.setId(1L);
        testTrip.setDestination("Paris");

        testParticipant = new TripParticipant();
        testParticipant.setUser(testUser);
        testParticipant.setTrip(testTrip);

        List<TripParticipant> participants = new ArrayList<>();
        participants.add(testParticipant);
        testTrip.setParticipants(participants);
    }

    @Test
    void testNotifyNewActivity() {
        // Arrange
        when(tripRepository.findById(1L)).thenReturn(Optional.of(testTrip));

        // Act
        notificationService.notifyNewActivity(
                1L,
                1L,
                "Test Activity",
                "Test Description",
                "2024-06-01"
        );

        // Assert
        verify(notificationRepository).save(any(Notification.class));
        verify(emailService).sendNotificationEmail(
                eq(testUser.getEmail()),
                anyString(),
                eq("email/new-activity"),
                any(Context.class)
        );
    }

    @Test
    void testNotifyNewVote() {
        // Arrange
        when(tripRepository.findById(1L)).thenReturn(Optional.of(testTrip));
        when(userRepository.findById(2L)).thenReturn(Optional.of(testUser));

        // Act
        notificationService.notifyNewVote(1L, 1L, 2L);

        // Assert
        verify(notificationRepository).save(any(Notification.class));
        verify(emailService).sendNotificationEmail(
                eq(testUser.getEmail()),
                anyString(),
                eq("email/new-vote"),
                any(Context.class)
        );
    }

    @Test
    void testGetUserNotifications() {
        // Arrange
        List<Notification> notifications = new ArrayList<>();
        when(userRepository.existsById(1L)).thenReturn(true);
        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(1L))
                .thenReturn(notifications);

        // Act
        List<Notification> result = notificationService.getUserNotifications(1L);

        // Assert
        assertNotNull(result);
        assertEquals(notifications, result);
    }

    @Test
    void testMarkAsRead() {
        // Arrange
        Notification notification = new Notification();
        notification.setId(1L);
        notification.setRead(false);

        when(notificationRepository.findById(1L))
                .thenReturn(Optional.of(notification));

        // Act
        notificationService.markAsRead(1L);

        // Assert
        verify(notificationRepository).save(any(Notification.class));
        assertTrue(notification.isRead());
    }
}