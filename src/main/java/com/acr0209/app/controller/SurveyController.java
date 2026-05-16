package com.acr0209.app.controller;

import com.acr0209.app.domain.Scenario;
import com.acr0209.app.dto.AwarenessForm;
import com.acr0209.app.dto.SurveyForm;
import com.acr0209.app.service.FeedbackService;
import com.acr0209.app.service.SurveyService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/survey")
public class SurveyController {

    private static final String PARTICIPANT_ID = "participantId";
    private static final String SCENARIO_ORDER = "scenarioOrder";
    private static final String CURRENT_INDEX = "currentScenarioIndex";
    private static final String STEP_STARTED_AT = "stepStartedAt";

    private final SurveyService surveyService;
    private final FeedbackService feedbackService;

    public SurveyController(SurveyService surveyService, FeedbackService feedbackService) {
        this.surveyService = surveyService;
        this.feedbackService = feedbackService;
    }

    @GetMapping("/start")
    public String start(HttpSession session) {
        session.setAttribute(PARTICIPANT_ID, UUID.randomUUID().toString());
        session.setAttribute(SCENARIO_ORDER, surveyService.createRandomScenarioOrder());
        session.setAttribute(CURRENT_INDEX, 0);
        session.setAttribute(STEP_STARTED_AT, Instant.now().toEpochMilli());
        return "redirect:/survey/current";
    }

    @GetMapping("/current")
    public String current(HttpSession session, Model model) {
        SurveyState state = getState(session);
        if (state.currentIndex() >= state.scenarioOrder().size()) {
            return "redirect:/survey/awareness";
        }
        String scenarioCode = state.scenarioOrder().get(state.currentIndex());
        Scenario scenario = surveyService.getScenario(scenarioCode);
        session.setAttribute(STEP_STARTED_AT, Instant.now().toEpochMilli());
        model.addAttribute("scenario", scenario);
        model.addAttribute("chatLines", scenario.getChatText().split("\\n"));
        model.addAttribute("summaryLines", scenario.getSituationSummary().split("\\n"));
        model.addAttribute("surveyForm", new SurveyForm());
        model.addAttribute("step", state.currentIndex() + 1);
        model.addAttribute("totalSteps", state.scenarioOrder().size());
        return "survey-form";
    }

    @PostMapping("/current")
    public String submit(
            HttpSession session,
            @Valid @ModelAttribute SurveyForm surveyForm,
            BindingResult bindingResult,
            Model model
    ) {
        SurveyState state = getState(session);
        if (state.currentIndex() >= state.scenarioOrder().size()) {
            return "redirect:/survey/awareness";
        }

        String scenarioCode = state.scenarioOrder().get(state.currentIndex());
        Scenario scenario = surveyService.getScenario(scenarioCode);

        if (bindingResult.hasErrors()) {
            model.addAttribute("scenario", scenario);
            model.addAttribute("chatLines", scenario.getChatText().split("\\n"));
            model.addAttribute("summaryLines", scenario.getSituationSummary().split("\\n"));
            model.addAttribute("step", state.currentIndex() + 1);
            model.addAttribute("totalSteps", state.scenarioOrder().size());
            return "survey-form";
        }

        long durationSeconds = calculateDurationSeconds(session);
        surveyService.saveResponse(state.participantId(), state.currentIndex() + 1, scenarioCode, durationSeconds, surveyForm);
        session.setAttribute(CURRENT_INDEX, state.currentIndex() + 1);

        if (state.currentIndex() + 1 >= state.scenarioOrder().size()) {
            return "redirect:/survey/awareness";
        }
        return "redirect:/survey/current";
    }

    @GetMapping("/awareness")
    public String awareness(HttpSession session, Model model) {
        getState(session);
        model.addAttribute("awarenessForm", new AwarenessForm());
        return "awareness-form";
    }

    @PostMapping("/awareness")
    public String submitAwareness(
            HttpSession session,
            @Valid @ModelAttribute AwarenessForm awarenessForm,
            BindingResult bindingResult
    ) {
        SurveyState state = getState(session);
        if (bindingResult.hasErrors()) {
            return "awareness-form";
        }
        surveyService.saveAwareness(state.participantId(), awarenessForm);
        return "redirect:/survey/thanks";
    }

    @GetMapping("/thanks")
    public String thanks(HttpSession session, Model model) {
        String participantId = (String) session.getAttribute(PARTICIPANT_ID);
        if (participantId != null) {
            model.addAttribute("feedback", feedbackService.buildFeedback(participantId));
        }
        session.removeAttribute(PARTICIPANT_ID);
        session.removeAttribute(SCENARIO_ORDER);
        session.removeAttribute(CURRENT_INDEX);
        session.removeAttribute(STEP_STARTED_AT);
        return "thanks";
    }

    private long calculateDurationSeconds(HttpSession session) {
        Object startedAt = session.getAttribute(STEP_STARTED_AT);
        if (startedAt instanceof Long startMillis) {
            return Math.max(0, (Instant.now().toEpochMilli() - startMillis) / 1000);
        }
        return 0;
    }

    @SuppressWarnings("unchecked")
    private SurveyState getState(HttpSession session) {
        String participantId = (String) session.getAttribute(PARTICIPANT_ID);
        List<String> scenarioOrder = (List<String>) session.getAttribute(SCENARIO_ORDER);
        Integer currentIndex = (Integer) session.getAttribute(CURRENT_INDEX);

        if (participantId == null || scenarioOrder == null || currentIndex == null) {
            session.setAttribute(PARTICIPANT_ID, UUID.randomUUID().toString());
            session.setAttribute(SCENARIO_ORDER, surveyService.createRandomScenarioOrder());
            session.setAttribute(CURRENT_INDEX, 0);
            session.setAttribute(STEP_STARTED_AT, Instant.now().toEpochMilli());
            return getState(session);
        }
        return new SurveyState(participantId, scenarioOrder, currentIndex);
    }

    private record SurveyState(String participantId, List<String> scenarioOrder, int currentIndex) {
    }
}
