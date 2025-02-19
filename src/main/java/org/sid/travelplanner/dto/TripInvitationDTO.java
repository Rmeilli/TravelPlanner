package org.sid.travelplanner.dto;

import lombok.Data;

import java.util.List;

@Data
public class TripInvitationDTO {
    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private List<String> emails;  // Emails des utilisateurs à inviter
    private String message;       // Message d'invitation optionnel
}
