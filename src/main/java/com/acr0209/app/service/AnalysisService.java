package com.acr0209.app.service;

import com.acr0209.app.domain.SurveyResponse;
import com.acr0209.app.dto.ScenarioSummary;
import com.acr0209.app.repository.SurveyResponseRepository;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnalysisService {

    private final SurveyResponseRepository surveyResponseRepository;

    public AnalysisService(SurveyResponseRepository surveyResponseRepository) {
        this.surveyResponseRepository = surveyResponseRepository;
    }

    @Transactional(readOnly = true)
    public long countResponses() {
        return surveyResponseRepository.count();
    }

    @Transactional(readOnly = true)
    public List<ScenarioSummary> summarizeByScenario() {
        return surveyResponseRepository.summarizeByScenario().stream()
                .map(row -> new ScenarioSummary(
                        (String) row[0],
                        (Long) row[1],
                        round((Double) row[2]),
                        round((Double) row[3]),
                        round((Double) row[4])
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public byte[] exportCsv() {
        List<SurveyResponse> responses = surveyResponseRepository.findAll();
        StringBuilder csv = new StringBuilder();
        csv.append("id,scenarioCode,motivationLevel,opportunityLevel,")
                .append("intentionQ1,intentionQ2,intentionQ3,")
                .append("justificationQ1,justificationQ2,justificationQ3,")
                .append("awarenessQ1,awarenessQ2,awarenessQ3,")
                .append("intentionScore,justificationScore,awarenessScore,createdAt\n");

        for (SurveyResponse r : responses) {
            csv.append(r.getId()).append(',')
                    .append(r.getScenarioCode()).append(',')
                    .append(r.getMotivationLevel()).append(',')
                    .append(r.getOpportunityLevel()).append(',')
                    .append(r.getIntentionQ1()).append(',')
                    .append(r.getIntentionQ2()).append(',')
                    .append(r.getIntentionQ3()).append(',')
                    .append(r.getJustificationQ1()).append(',')
                    .append(r.getJustificationQ2()).append(',')
                    .append(r.getJustificationQ3()).append(',')
                    .append(r.getAwarenessQ1()).append(',')
                    .append(r.getAwarenessQ2()).append(',')
                    .append(r.getAwarenessQ3()).append(',')
                    .append(r.getIntentionScore()).append(',')
                    .append(r.getJustificationScore()).append(',')
                    .append(r.getAwarenessScore()).append(',')
                    .append(r.getCreatedAt()).append('\n');
        }
        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
