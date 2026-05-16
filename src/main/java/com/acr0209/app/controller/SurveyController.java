package com.acr0209.app.controller;

import com.acr0209.app.domain.Scenario;
import com.acr0209.app.dto.SurveyForm;
import com.acr0209.app.service.SurveyService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @GetMapping("/start")
    public String start(HttpSession session) {
        session.setAttribute(PARTICIPANT_ID, UUID.randomUUID().toString());
        session.setAttribute(SCENARIO_ORDER, surveyService.createRandomScenarioOrder());
        session.setAttribute(CURRENT_INDEX, 0);
        return "redirect:/survey/current";
    }

    @GetMapping("/current")
    public String current(HttpSession session, Model model) {
        SurveyState state = getState(session);
        if (state.currentIndex() >= state.scenarioOrder().size()) {
            return "redirect:/survey/thanks";
        }
        String scenarioCode = state.scenarioOrder().get(state.currentIndex());
        Scenario scenario = surveyService.getScenario(scenarioCode);
        model.addAttribute("scenario", scenario);
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
            return "redirect:/survey/thanks";
        }

        String scenarioCode = state.scenarioOrder().get(state.currentIndex());
        Scenario scenario = surveyService.getScenario(scenarioCode);

        if (bindingResult.hasErrors()) {
            model.addAttribute("scenario", scenario);
            model.addAttribute("step", state.currentIndex() + 1);
            model.addAttribute("totalSteps", state.scenarioOrder().size());
            return "survey-form";
        }

        surveyService.saveResponse(state.participantId(), state.currentIndex() + 1, scenarioCode, surveyForm);
        session.setAttribute(CURRENT_INDEX, state.currentIndex() + 1);

        if (state.currentIndex() + 1 >= state.scenarioOrder().size()) {
            return "redirect:/survey/thanks";
        }
        return "redirect:/survey/current";
    }

    @GetMapping("/thanks")
    public String thanks(HttpSession session) {
        session.removeAttribute(PARTICIPANT_ID);
        session.removeAttribute(SCENARIO_ORDER);
        session.removeAttribute(CURRENT_INDEX);
        return "thanks";
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
            return getState(session);
        }
        return new SurveyState(participantId, scenarioOrder, currentIndex);
    }

    private record SurveyState(String participantId, List<String> scenarioOrder, int currentIndex) {
    }
}
