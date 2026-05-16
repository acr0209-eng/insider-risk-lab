package com.acr0209.app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "scenario")
public class Scenario {

    @Id
    private String code;

    @Column(nullable = false)
    private String motivationLevel;

    @Column(nullable = false)
    private String opportunityLevel;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String imagePath;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String chatText;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String situationSummary;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    protected Scenario() {
    }

    public Scenario(String code, String motivationLevel, String opportunityLevel, String title, String imagePath, String chatText, String situationSummary, String content) {
        this.code = code;
        this.motivationLevel = motivationLevel;
        this.opportunityLevel = opportunityLevel;
        this.title = title;
        this.imagePath = imagePath;
        this.chatText = chatText;
        this.situationSummary = situationSummary;
        this.content = content;
    }

    public String getCode() { return code; }
    public String getMotivationLevel() { return motivationLevel; }
    public String getOpportunityLevel() { return opportunityLevel; }
    public String getTitle() { return title; }
    public String getImagePath() { return imagePath; }
    public String getChatText() { return chatText; }
    public String getSituationSummary() { return situationSummary; }
    public String getContent() { return content; }
}
