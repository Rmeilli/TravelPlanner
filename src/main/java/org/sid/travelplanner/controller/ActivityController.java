package org.sid.travelplanner.controller;
import lombok.RequiredArgsConstructor;
import org.sid.travelplanner.dto.ActivityDTO;
import org.sid.travelplanner.model.Activity;
import org.sid.travelplanner.model.ActivityStatus;
import org.sid.travelplanner.service.ActivityService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/trips/{tripId}/activities")

public class ActivityController {
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    private final ActivityService activityService;

    @PostMapping
    public ResponseEntity<Activity> createActivity(
            @PathVariable Long tripId,
            @RequestParam Long userId,
            @RequestBody ActivityDTO activityDTO) {
        return ResponseEntity.ok(activityService.createActivity(tripId, userId, activityDTO));
    }

    @GetMapping
    public ResponseEntity<List<Activity>> getTripActivities(@PathVariable Long tripId) {
        return ResponseEntity.ok(activityService.getTripActivities(tripId));
    }

    @GetMapping("/{activityId}")
    public ResponseEntity<Activity> getActivity(
            @PathVariable Long tripId,
            @PathVariable Long activityId) {
        return ResponseEntity.ok(activityService.getActivity(activityId));
    }

    @PutMapping("/{activityId}")
    public ResponseEntity<Activity> updateActivity(
            @PathVariable Long tripId,
            @PathVariable Long activityId,
            @RequestBody ActivityDTO activityDTO) {
        return ResponseEntity.ok(activityService.updateActivity(activityId, activityDTO));
    }

    @DeleteMapping("/{activityId}")
    public ResponseEntity<Void> deleteActivity(
            @PathVariable Long tripId,
            @PathVariable Long activityId) {
        activityService.deleteActivity(activityId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/daily")
    public ResponseEntity<List<Activity>> getDailyActivities(
            @PathVariable Long tripId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        return ResponseEntity.ok(activityService.getDailyActivities(tripId, date));
    }

    @PatchMapping("/{activityId}/status")
    public ResponseEntity<Void> updateActivityStatus(
            @PathVariable Long tripId,
            @PathVariable Long activityId,
            @RequestParam ActivityStatus status) {
        activityService.updateActivityStatus(activityId, status);
        return ResponseEntity.ok().build();
    }
}
