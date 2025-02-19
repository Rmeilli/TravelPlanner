package org.sid.travelplanner.TEST;

import lombok.RequiredArgsConstructor;
import org.sid.travelplanner.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test/notifications")
public class TestNotificationController {
    public TestNotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    private final NotificationService notificationService;

    @PostMapping("/activity")
    public ResponseEntity<String> testActivityNotification(
            @RequestParam Long tripId,
            @RequestParam Long activityId,
            @RequestParam String activityName,
            @RequestParam String activityDescription,
            @RequestParam String activityDate) {

        notificationService.notifyNewActivity(
                tripId,
                activityId,
                activityName,
                activityDescription,
                activityDate
        );

        return ResponseEntity.ok("Notifications d'activité envoyées");
    }

    @PostMapping("/vote")
    public ResponseEntity<String> testVoteNotification(
            @RequestParam Long tripId,
            @RequestParam Long activityId,
            @RequestParam Long voterId) {

        notificationService.notifyNewVote(tripId, activityId, voterId);
        return ResponseEntity.ok("Notifications de vote envoyées");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<?> getUnreadNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(userId));
    }

    @PostMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/{userId}/read-all")
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }
}
