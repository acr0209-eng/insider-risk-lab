package com.acr0209.app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "survey_response")
public class SurveyResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String participantId;

    @Column(nullable = false)
    private int scenarioOrder;

    private String scenarioCode;
    private String motivationLevel;
    private String opportunityLevel;

    private long durationSeconds;
    private boolean tooFast;
    private boolean straightLined;

    private int intentionQ1;
    private int intentionQ2;
    private int intentionQ3;

    private int justificationQ1;
    private int justificationQ2;
    private int justificationQ3;

    private double intentionScore;
    private double justificationScore;

    private LocalDateTime createdAt;

    protected SurveyResponse() {
    }

    public SurveyResponse(
            String participantId,
            int scenarioOrder,
            String scenarioCode,
            String motivationLevel,
            String opportunityLevel,
            long durationSeconds,
            int intentionQ1,
            int intentionQ2,
            int intentionQ3,
            int justificationQ1,
            int justificationQ2,
            int justificationQ3
    ) {
        this.participantId = participantId;
        this.scenarioOrder = scenarioOrder;
        this.scenarioCode = scenarioCode;
        this.motivationLevel = motivationLevel;
        this.opportunityLevel = opportunityLevel;
        this.durationSeconds = durationSeconds;
        this.intentionQ1 = intentionQ1;
        this.intentionQ2 = intentionQ2;
        this.intentionQ3 = intentionQ3;
        this.justificationQ1 = justificationQ1;
        this.justificationQ2 = justificationQ2;
        this.justificationQ3 = justificationQ3;
        this.intentionScore = average(intentionQ1, intentionQ2, intentionQ3);
        this.justificationScore = average(justificationQ1, justificationQ2, justificationQ3);
        this.tooFast = durationSeconds > 0 && durationSeconds < 8;
        this.straightLined = allSame(intentionQ1, intentionQ2, intentionQ3, justificationQ1, justificationQ2, justificationQ3);
        this.createdAt = LocalDateTime.now();
    }

    private double average(int a, int b, int c) {
        return (a + b + c) / 3.0;
    }

    private boolean allSame(int... values) {
        for (int value : values) {
            if (value != values[0]) return false;
        }
        return true;
    }

    public Long getId() { return id; }
    public String getParticipantId() { return participantId; }
    public int getScenarioOrder() { return scenarioOrder; }
    public String getScenarioCode() { return scenarioCode; }
    public String getMotivationLevel() { return motivationLevel; }
    public String getOpportunityLevel() { return opportunityLevel; }
    public long getDurationSeconds() { return durationSeconds; }
    public boolean isTooFast() { return tooFast; }
    public boolean isStraightLined() { return straightLined; }
    public int getIntentionQ1() { return intentionQ1; }
    public int getIntentionQ2() { return intentionQ2; }
    public int getIntentionQ3() { return intentionQ3; }
    public int getJustificationQ1() { return justificationQ1; }
    public int getJustificationQ2() { return justificationQ2; }
    public int getJustificationQ3() { return justificationQ3; }
    public double getIntentionScore() { return intentionScore; }
    public double getJustificationScore() { return justificationScore; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
