package org.sid.travelplanner.repository;

import org.sid.travelplanner.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Notification> findByUserIdAndReadOrderByCreatedAtDesc(Long userId, boolean read);
    List<Notification> findByTripIdAndUserIdOrderByCreatedAtDesc(Long tripId, Long userId);
    long countByUserIdAndRead(Long userId, boolean read);
}
