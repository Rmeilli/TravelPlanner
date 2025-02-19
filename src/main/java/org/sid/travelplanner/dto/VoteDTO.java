package org.sid.travelplanner.dto;

import lombok.Data;

@Data
public class VoteDTO {
    public Integer getVote() {
        return vote;
    }

    public void setVote(Integer vote) {
        this.vote = vote;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    private Integer vote;
    private String comment;
}
