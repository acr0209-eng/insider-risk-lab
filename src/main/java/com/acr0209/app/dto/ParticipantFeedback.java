package com.acr0209.app.dto;

public record ParticipantFeedback(
        String profileType,
        String interpretation,
        double motivationEffect,
        double opportunityEffect,
        double combinedEffect,
        double averageJustification,
        String mainSwitch,
        String preventionTip
) {
}
