package org.sid.travelplanner.service;

import lombok.RequiredArgsConstructor;
import org.sid.travelplanner.dto.VoteDTO;
import org.sid.travelplanner.model.Activity;
import org.sid.travelplanner.model.ActivityVote;
import org.sid.travelplanner.model.User;
import org.sid.travelplanner.repository.ActivityRepository;
import org.sid.travelplanner.repository.ActivityVoteRepository;
import org.sid.travelplanner.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ActivityVoteService {
    public ActivityVoteService(ActivityVoteRepository voteRepository, ActivityRepository activityRepository, UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
    }

    private final ActivityVoteRepository voteRepository;
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    public ActivityVote vote(Long activityId, Long userId, VoteDTO voteDTO) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activité non trouvée"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        ActivityVote vote = voteRepository.findByActivityIdAndUserId(activityId, userId)
                .orElse(new ActivityVote());

        vote.setActivity(activity);
        vote.setUser(user);
        vote.setVote(voteDTO.getVote());
        vote.setComment(voteDTO.getComment());

        return voteRepository.save(vote);
    }

    public List<ActivityVote> getActivityVotes(Long activityId) {
        return voteRepository.findByActivityId(activityId);
    }

    public void deleteVote(Long activityId, Long userId) {
        ActivityVote vote = voteRepository.findByActivityIdAndUserId(activityId, userId)
                .orElseThrow(() -> new RuntimeException("Vote non trouvé"));
        voteRepository.delete(vote);
    }

    public Map<Long, Integer> getActivityVoteScores(List<Long> activityIds) {
        return activityIds.stream()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> voteRepository.calculateVoteScore(id)
                ));
    }

    public Integer getActivityVoteScore(Long activityId) {
        return voteRepository.calculateVoteScore(activityId);
    }
}
