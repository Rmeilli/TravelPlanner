package org.sid.travelplanner.controller;

import lombok.RequiredArgsConstructor;
import org.sid.travelplanner.dto.VoteDTO;
import org.sid.travelplanner.model.ActivityVote;
import org.sid.travelplanner.service.ActivityVoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trips/{tripId}/activities/{activityId}/votes")
public class ActivityVoteController {
    public ActivityVoteController(ActivityVoteService voteService) {
        this.voteService = voteService;
    }

    private final ActivityVoteService voteService;

    @PostMapping
    public ResponseEntity<ActivityVote> vote(
            @PathVariable Long tripId,
            @PathVariable Long activityId,
            @RequestParam Long userId,
            @RequestBody VoteDTO voteDTO) {
        return ResponseEntity.ok(voteService.vote(activityId, userId, voteDTO));
    }

    @GetMapping
    public ResponseEntity<List<ActivityVote>> getActivityVotes(
            @PathVariable Long tripId,
            @PathVariable Long activityId) {
        return ResponseEntity.ok(voteService.getActivityVotes(activityId));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteVote(
            @PathVariable Long tripId,
            @PathVariable Long activityId,
            @RequestParam Long userId) {
        voteService.deleteVote(activityId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/score")
    public ResponseEntity<Integer> getActivityVoteScore(
            @PathVariable Long tripId,
            @PathVariable Long activityId) {
        return ResponseEntity.ok(voteService.getActivityVoteScore(activityId));
    }
}
