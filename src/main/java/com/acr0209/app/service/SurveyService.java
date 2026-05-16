package com.acr0209.app.service;

import com.acr0209.app.domain.Scenario;
import com.acr0209.app.domain.SurveyResponse;
import com.acr0209.app.dto.SurveyForm;
import com.acr0209.app.repository.ScenarioRepository;
import com.acr0209.app.repository.SurveyResponseRepository;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SurveyService {

    private final ScenarioRepository scenarioRepository;
    private final SurveyResponseRepository surveyResponseRepository;
    private final Random random = new Random();

    public SurveyService(ScenarioRepository scenarioRepository, SurveyResponseRepository surveyResponseRepository) {
        this.scenarioRepository = scenarioRepository;
        this.surveyResponseRepository = surveyResponseRepository;
    }

    @Transactional(readOnly = true)
    public Scenario getRandomScenario() {
        List<Scenario> scenarios = scenarioRepository.findAll();
        if (scenarios.isEmpty()) {
            throw new IllegalStateException("No scenarios are registered. Check data.sql.");
        }
        return scenarios.get(random.nextInt(scenarios.size()));
    }

    @Transactional(readOnly = true)
    public Scenario getScenario(String code) {
        return scenarioRepository.findById(code)
                .orElseThrow(() -> new IllegalArgumentException("Unknown scenario code: " + code));
    }

    @Transactional
    public SurveyResponse saveResponse(String scenarioCode, SurveyForm form) {
        Scenario scenario = getScenario(scenarioCode);
        SurveyResponse response = new SurveyResponse(
                scenario.getCode(),
                scenario.getMotivationLevel(),
                scenario.getOpportunityLevel(),
                form.getIntentionQ1(),
                form.getIntentionQ2(),
                form.getIntentionQ3(),
                form.getJustificationQ1(),
                form.getJustificationQ2(),
                form.getJustificationQ3(),
                form.getAwarenessQ1(),
                form.getAwarenessQ2(),
                form.getAwarenessQ3()
        );
        return surveyResponseRepository.save(response);
    }
}
