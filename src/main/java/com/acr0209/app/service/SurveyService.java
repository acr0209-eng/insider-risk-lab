package com.acr0209.app.service;

import com.acr0209.app.domain.ParticipantProfile;
import com.acr0209.app.domain.Scenario;
import com.acr0209.app.domain.SurveyResponse;
import com.acr0209.app.dto.AwarenessForm;
import com.acr0209.app.dto.SurveyForm;
import com.acr0209.app.repository.ParticipantProfileRepository;
import com.acr0209.app.repository.ScenarioRepository;
import com.acr0209.app.repository.SurveyResponseRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SurveyService {

    private final ScenarioRepository scenarioRepository;
    private final SurveyResponseRepository surveyResponseRepository;
    private final ParticipantProfileRepository participantProfileRepository;

    public SurveyService(
            ScenarioRepository scenarioRepository,
            SurveyResponseRepository surveyResponseRepository,
            ParticipantProfileRepository participantProfileRepository
    ) {
        this.scenarioRepository = scenarioRepository;
        this.surveyResponseRepository = surveyResponseRepository;
        this.participantProfileRepository = participantProfileRepository;
    }

    @Transactional(readOnly = true)
    public List<String> createRandomScenarioOrder() {
        List<String> codes = new ArrayList<>(scenarioRepository.findAll().stream()
                .map(Scenario::getCode)
                .sorted()
                .toList());
        if (codes.size() != 4) {
            throw new IllegalStateException("Exactly 4 scenarios are required. Check data.sql.");
        }
        Collections.shuffle(codes);
        return codes;
    }

    @Transactional(readOnly = true)
    public Scenario getScenario(String code) {
        return scenarioRepository.findById(code)
                .orElseThrow(() -> new IllegalArgumentException("Unknown scenario code: " + code));
    }

    @Transactional
    public SurveyResponse saveResponse(String participantId, int scenarioOrder, String scenarioCode, long durationSeconds, SurveyForm form) {
        Scenario scenario = getScenario(scenarioCode);
        SurveyResponse response = new SurveyResponse(
                participantId,
                scenarioOrder,
                scenario.getCode(),
                scenario.getMotivationLevel(),
                scenario.getOpportunityLevel(),
                form.getActionChoice(),
                durationSeconds,
                form.getIntentionQ1(),
                form.getIntentionQ2(),
                form.getIntentionQ3(),
                form.getJustificationQ1(),
                form.getJustificationQ2(),
                form.getJustificationQ3()
        );
        return surveyResponseRepository.save(response);
    }

    @Transactional
    public ParticipantProfile saveAwareness(String participantId, AwarenessForm form) {
        ParticipantProfile profile = new ParticipantProfile(
                participantId,
                form.getParticipantName().trim(),
                form.getAwarenessQ1(),
                form.getAwarenessQ2(),
                form.getAwarenessQ3()
        );
        return participantProfileRepository.save(profile);
    }
}
