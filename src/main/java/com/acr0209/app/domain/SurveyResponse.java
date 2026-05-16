package com.acr0209.app.domain;

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

    private String scenarioCode;
    private String motivationLevel;
    private String opportunityLevel;

    private int intentionQ1;
    private int intentionQ2;
    private int intentionQ3;

    private int justificationQ1;
    private int justificationQ2;
    private int justificationQ3;

    private int awarenessQ1;
    private int awarenessQ2;
    private int awarenessQ3;

    private double intentionScore;
    private double justificationScore;
    private double awarenessScore;

    private LocalDateTime createdAt;

    protected SurveyResponse() {
    }

    public SurveyResponse(
            String scenarioCode,
            String motivationLevel,
            String opportunityLevel,
            int intentionQ1,
            int intentionQ2,
            int intentionQ3,
            int justificationQ1,
            int justificationQ2,
            int justificationQ3,
            int awarenessQ1,
            int awarenessQ2,
            int awarenessQ3
    ) {
        this.scenarioCode = scenarioCode;
        this.motivationLevel = motivationLevel;
        this.opportunityLevel = opportunityLevel;
        this.intentionQ1 = intentionQ1;
        this.intentionQ2 = intentionQ2;
        this.intentionQ3 = intentionQ3;
        this.justificationQ1 = justificationQ1;
        this.justificationQ2 = justificationQ2;
        this.justificationQ3 = justificationQ3;
        this.awarenessQ1 = awarenessQ1;
        this.awarenessQ2 = awarenessQ2;
        this.awarenessQ3 = awarenessQ3;
        this.intentionScore = average(intentionQ1, intentionQ2, intentionQ3);
        this.justificationScore = average(justificationQ1, justificationQ2, justificationQ3);
        this.awarenessScore = average(awarenessQ1, awarenessQ2, awarenessQ3);
        this.createdAt = LocalDateTime.now();
    }

    private double average(int a, int b, int c) {
        return (a + b + c) / 3.0;
    }

    public Long getId() { return id; }
    public String getScenarioCode() { return scenarioCode; }
    public String getMotivationLevel() { return motivationLevel; }
    public String getOpportunityLevel() { return opportunityLevel; }
    public int getIntentionQ1() { return intentionQ1; }
    public int getIntentionQ2() { return intentionQ2; }
    public int getIntentionQ3() { return intentionQ3; }
    public int getJustificationQ1() { return justificationQ1; }
    public int getJustificationQ2() { return justificationQ2; }
    public int getJustificationQ3() { return justificationQ3; }
    public int getAwarenessQ1() { return awarenessQ1; }
    public int getAwarenessQ2() { return awarenessQ2; }
    public int getAwarenessQ3() { return awarenessQ3; }
    public double getIntentionScore() { return intentionScore; }
    public double getJustificationScore() { return justificationScore; }
    public double getAwarenessScore() { return awarenessScore; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
