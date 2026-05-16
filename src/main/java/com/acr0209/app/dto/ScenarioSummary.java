package com.acr0209.app.dto;

public record ScenarioSummary(
        String scenarioCode,
        long responseCount,
        double averageIntention,
        double averageJustification,
        double averageAwareness
) {
}
