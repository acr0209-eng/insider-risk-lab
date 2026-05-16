package com.acr0209.app.service;

import com.acr0209.app.domain.SurveyResponse;
import com.acr0209.app.dto.ScenarioSummary;
import com.acr0209.app.repository.ParticipantProfileRepository;
import com.acr0209.app.repository.SurveyResponseRepository;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnalysisService {

    private final SurveyResponseRepository surveyResponseRepository;
    private final ParticipantProfileRepository participantProfileRepository;

    public AnalysisService(
            SurveyResponseRepository surveyResponseRepository,
            ParticipantProfileRepository participantProfileRepository
    ) {
        this.surveyResponseRepository = surveyResponseRepository;
        this.participantProfileRepository = participantProfileRepository;
    }

    @Transactional(readOnly = true)
    public long countResponses() {
        return surveyResponseRepository.count();
    }

    @Transactional(readOnly = true)
    public long countParticipants() {
        return surveyResponseRepository.countDistinctParticipants();
    }

    @Transactional(readOnly = true)
    public long countCompletedParticipants() {
        return participantProfileRepository.count();
    }

    @Transactional(readOnly = true)
    public long countTooFastResponses() {
        return surveyResponseRepository.countByTooFastTrue();
    }

    @Transactional(readOnly = true)
    public long countStraightLinedResponses() {
        return surveyResponseRepository.countByStraightLinedTrue();
    }

    @Transactional(readOnly = true)
    public double averageDurationSeconds() {
        Double value = surveyResponseRepository.averageDurationSeconds();
        return value == null ? 0.0 : round(value);
    }

    @Transactional(readOnly = true)
    public double averageAwarenessScore() {
        Double value = participantProfileRepository.averageAwarenessScore();
        return value == null ? 0.0 : round(value);
    }

    @Transactional(readOnly = true)
    public double completionRate() {
        long participants = countParticipants();
        if (participants == 0) return 0.0;
        return round(countCompletedParticipants() * 100.0 / participants);
    }

    @Transactional(readOnly = true)
    public List<ScenarioSummary> summarizeByScenario() {
        return surveyResponseRepository.summarizeByScenario().stream()
                .map(row -> new ScenarioSummary(
                        (String) row[0],
                        (Long) row[1],
                        round((Double) row[2]),
                        round((Double) row[3])
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public Map<String, Double> intentionDifferences() {
        Map<String, Double> scores = new HashMap<>();
        for (ScenarioSummary summary : summarizeByScenario()) {
            scores.put(summary.scenarioCode(), summary.averageIntention());
        }
        Map<String, Double> diff = new HashMap<>();
        diff.put("A-D", round(scores.getOrDefault("A", 0.0) - scores.getOrDefault("D", 0.0)));
        diff.put("A-B", round(scores.getOrDefault("A", 0.0) - scores.getOrDefault("B", 0.0)));
        diff.put("A-C", round(scores.getOrDefault("A", 0.0) - scores.getOrDefault("C", 0.0)));
        return diff;
    }

    @Transactional(readOnly = true)
    public String reportSummary() {
        List<ScenarioSummary> summaries = summarizeByScenario();
        if (summaries.isEmpty()) {
            return "아직 응답 데이터가 없습니다. 설문 응답이 쌓이면 보고서용 요약문이 자동 생성됩니다.";
        }
        ScenarioSummary highest = summaries.stream()
                .max((a, b) -> Double.compare(a.averageIntention(), b.averageIntention()))
                .orElseThrow();
        ScenarioSummary lowest = summaries.stream()
                .min((a, b) -> Double.compare(a.averageIntention(), b.averageIntention()))
                .orElseThrow();
        Map<String, Double> diff = intentionDifferences();
        return "현재까지 총 " + countCompletedParticipants() + "명의 완료 참여자와 " + countResponses()
                + "개의 시나리오 응답이 수집되었다. 반출 의향 평균은 " + highest.scenarioCode()
                + " 조건에서 " + highest.averageIntention() + "점으로 가장 높았고, " + lowest.scenarioCode()
                + " 조건에서 " + lowest.averageIntention() + "점으로 가장 낮았다. A-D 평균 차이는 "
                + diff.get("A-D") + "점으로, 업무상 동기와 보안위반 기회가 함께 존재할 때 내부자료 반출 의향이 증가하는지 검토할 수 있다.";
    }

    @Transactional(readOnly = true)
    public byte[] exportCsv() {
        List<SurveyResponse> responses = surveyResponseRepository.findAll();
        StringBuilder csv = new StringBuilder();
        csv.append("id,participantId,scenarioOrder,scenarioCode,motivationLevel,opportunityLevel,")
                .append("durationSeconds,tooFast,straightLined,")
                .append("intentionQ1,intentionQ2,intentionQ3,")
                .append("justificationQ1,justificationQ2,justificationQ3,")
                .append("intentionScore,justificationScore,createdAt\n");

        for (SurveyResponse r : responses) {
            csv.append(r.getId()).append(',')
                    .append(r.getParticipantId()).append(',')
                    .append(r.getScenarioOrder()).append(',')
                    .append(r.getScenarioCode()).append(',')
                    .append(r.getMotivationLevel()).append(',')
                    .append(r.getOpportunityLevel()).append(',')
                    .append(r.getDurationSeconds()).append(',')
                    .append(r.isTooFast()).append(',')
                    .append(r.isStraightLined()).append(',')
                    .append(r.getIntentionQ1()).append(',')
                    .append(r.getIntentionQ2()).append(',')
                    .append(r.getIntentionQ3()).append(',')
                    .append(r.getJustificationQ1()).append(',')
                    .append(r.getJustificationQ2()).append(',')
                    .append(r.getJustificationQ3()).append(',')
                    .append(r.getIntentionScore()).append(',')
                    .append(r.getJustificationScore()).append(',')
                    .append(r.getCreatedAt()).append('\n');
        }
        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
