package com.acr0209.app.controller;

import com.acr0209.app.service.AnalysisService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AnalysisService analysisService;

    public AdminController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalParticipants", analysisService.countParticipants());
        model.addAttribute("completedParticipants", analysisService.countCompletedParticipants());
        model.addAttribute("completionRate", analysisService.completionRate());
        model.addAttribute("totalResponses", analysisService.countResponses());
        model.addAttribute("averageDurationSeconds", analysisService.averageDurationSeconds());
        model.addAttribute("tooFastResponses", analysisService.countTooFastResponses());
        model.addAttribute("straightLinedResponses", analysisService.countStraightLinedResponses());
        model.addAttribute("averageAwarenessScore", analysisService.averageAwarenessScore());
        model.addAttribute("summaries", analysisService.summarizeByScenario());
        model.addAttribute("diff", analysisService.intentionDifferences());
        model.addAttribute("actionChoiceCounts", analysisService.actionChoiceCounts());
        model.addAttribute("reportSummary", analysisService.reportSummary());
        return "admin-dashboard";
    }

    @GetMapping("/responses.csv")
    public ResponseEntity<byte[]> downloadCsv() {
        byte[] csv = analysisService.exportCsv();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "csv"));
        headers.setContentDisposition(ContentDisposition.attachment().filename("insider-risk-responses.csv").build());
        return ResponseEntity.ok().headers(headers).body(csv);
    }
}
