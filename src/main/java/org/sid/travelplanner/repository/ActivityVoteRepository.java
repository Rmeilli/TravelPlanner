package org.sid.travelplanner.repository;

import org.sid.travelplanner.model.ActivityVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ActivityVoteRepository extends JpaRepository<ActivityVote, Long> {
    Optional<ActivityVote> findByActivityIdAndUserId(Long activityId, Long userId);
    List<ActivityVote> findByActivityId(Long activityId);

    @Query("SELECT COALESCE(SUM(v.vote), 0) FROM ActivityVote v WHERE v.activity.id = :activityId")
    Integer calculateVoteScore(Long activityId);
}
