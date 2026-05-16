package com.acr0209.app.controller;

import com.acr0209.app.domain.Scenario;
import com.acr0209.app.dto.SurveyForm;
import com.acr0209.app.service.SurveyService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/survey")
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @GetMapping("/start")
    public String start() {
        Scenario scenario = surveyService.getRandomScenario();
        return "redirect:/survey/" + scenario.getCode();
    }

    @GetMapping("/{scenarioCode}")
    public String form(@PathVariable String scenarioCode, Model model) {
        model.addAttribute("scenario", surveyService.getScenario(scenarioCode));
        model.addAttribute("surveyForm", new SurveyForm());
        return "survey-form";
    }

    @PostMapping("/{scenarioCode}")
    public String submit(
            @PathVariable String scenarioCode,
            @Valid @ModelAttribute SurveyForm surveyForm,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("scenario", surveyService.getScenario(scenarioCode));
            return "survey-form";
        }
        surveyService.saveResponse(scenarioCode, surveyForm);
        return "redirect:/survey/thanks";
    }

    @GetMapping("/thanks")
    public String thanks() {
        return "thanks";
    }
}
