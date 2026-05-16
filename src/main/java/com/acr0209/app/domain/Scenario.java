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

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    protected Scenario() {
    }

    public Scenario(String code, String motivationLevel, String opportunityLevel, String title, String content) {
        this.code = code;
        this.motivationLevel = motivationLevel;
        this.opportunityLevel = opportunityLevel;
        this.title = title;
        this.content = content;
    }

    public String getCode() {
        return code;
    }

    public String getMotivationLevel() {
        return motivationLevel;
    }

    public String getOpportunityLevel() {
        return opportunityLevel;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
