package org.sid.travelplanner.service;


import lombok.RequiredArgsConstructor;
import org.sid.travelplanner.dto.ActivityDTO;
import org.sid.travelplanner.model.Activity;
import org.sid.travelplanner.model.ActivityStatus;
import org.sid.travelplanner.model.Trip;
import org.sid.travelplanner.model.User;
import org.sid.travelplanner.repository.ActivityRepository;
import org.sid.travelplanner.repository.TripRepository;
import org.sid.travelplanner.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.time.LocalDateTime;

@Service
@Transactional
public class ActivityService {
    public ActivityService(ActivityRepository activityRepository, TripRepository tripRepository, UserRepository userRepository) {
        this.activityRepository = activityRepository;
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
    }

    private final ActivityRepository activityRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    public Activity createActivity(Long tripId, Long userId, ActivityDTO activityDTO) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Voyage non trouvé"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Activity activity = new Activity();
        activity.setTrip(trip);
        activity.setName(activityDTO.getName());
        activity.setDescription(activityDTO.getDescription());
        activity.setStartDateTime(activityDTO.getStartDateTime());
        activity.setEndDateTime(activityDTO.getEndDateTime());
        activity.setLocation(activityDTO.getLocation());
        activity.setEstimatedCost(activityDTO.getEstimatedCost());
        activity.setCreatedBy(user);

        return activityRepository.save(activity);
    }

    public List<Activity> getTripActivities(Long tripId) {
        return activityRepository.findByTripId(tripId);
    }

    public Activity getActivity(Long activityId) {
        return activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activité non trouvée"));
    }

    public Activity updateActivity(Long activityId, ActivityDTO activityDTO) {
        Activity activity = getActivity(activityId);

        activity.setName(activityDTO.getName());
        activity.setDescription(activityDTO.getDescription());
        activity.setStartDateTime(activityDTO.getStartDateTime());
        activity.setEndDateTime(activityDTO.getEndDateTime());
        activity.setLocation(activityDTO.getLocation());
        activity.setEstimatedCost(activityDTO.getEstimatedCost());

        return activityRepository.save(activity);
    }

    public void deleteActivity(Long activityId) {
        activityRepository.deleteById(activityId);
    }

    public List<Activity> getDailyActivities(Long tripId, LocalDateTime date) {
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return activityRepository.findByTripIdAndStartDateTimeBetween(tripId, startOfDay, endOfDay);
    }

    public void updateActivityStatus(Long activityId, ActivityStatus status) {
        Activity activity = getActivity(activityId);
        activity.setStatus(status);
        activityRepository.save(activity);
    }
}
