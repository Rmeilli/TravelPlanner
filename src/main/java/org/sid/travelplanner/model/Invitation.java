package org.sid.travelplanner.model;

// src/main/java/com/example/travelplanner/model/Invitation.java

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "invitations")
public class Invitation {
    public void setId(Long id) {
        this.id = id;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public void setInvitedEmail(String invitedEmail) {
        this.invitedEmail = invitedEmail;
    }

    public void setInviter(User inviter) {
        this.inviter = inviter;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setStatus(InvitationStatus status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setRespondedAt(LocalDateTime respondedAt) {
        this.respondedAt = respondedAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Long getId() {
        return id;
    }

    public Trip getTrip() {
        return trip;
    }

    public String getInvitedEmail() {
        return invitedEmail;
    }

    public User getInviter() {
        return inviter;
    }

    public String getToken() {
        return token;
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getRespondedAt() {
        return respondedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(nullable = false)
    private String invitedEmail;

    @ManyToOne
    @JoinColumn(name = "inviter_user_id", nullable = false)
    private User inviter;

    @Column(nullable = false, unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvitationStatus status = InvitationStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime respondedAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;
}
