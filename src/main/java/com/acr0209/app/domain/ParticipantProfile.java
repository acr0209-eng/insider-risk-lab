package com.acr0209.app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "participant_profile")
public class ParticipantProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String participantId;

    private String participantName;
    private int awarenessQ1;
    private int awarenessQ2;
    private int awarenessQ3;
    private double awarenessScore;
    private LocalDateTime createdAt;

    protected ParticipantProfile() {
    }

    public ParticipantProfile(String participantId, String participantName, int awarenessQ1, int awarenessQ2, int awarenessQ3) {
        this.participantId = participantId;
        this.participantName = participantName;
        this.awarenessQ1 = awarenessQ1;
        this.awarenessQ2 = awarenessQ2;
        this.awarenessQ3 = awarenessQ3;
        this.awarenessScore = (awarenessQ1 + awarenessQ2 + awarenessQ3) / 3.0;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getParticipantId() { return participantId; }
    public String getParticipantName() { return participantName; }
    public int getAwarenessQ1() { return awarenessQ1; }
    public int getAwarenessQ2() { return awarenessQ2; }
    public int getAwarenessQ3() { return awarenessQ3; }
    public double getAwarenessScore() { return awarenessScore; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
