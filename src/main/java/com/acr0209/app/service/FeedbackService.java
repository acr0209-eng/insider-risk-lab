package com.acr0209.app.service;

import com.acr0209.app.domain.SurveyResponse;
import com.acr0209.app.dto.ParticipantFeedback;
import com.acr0209.app.repository.SurveyResponseRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedbackService {

    private final SurveyResponseRepository surveyResponseRepository;

    public FeedbackService(SurveyResponseRepository surveyResponseRepository) {
        this.surveyResponseRepository = surveyResponseRepository;
    }

    @Transactional(readOnly = true)
    public ParticipantFeedback buildFeedback(String participantId) {
        List<SurveyResponse> responses = surveyResponseRepository.findByParticipantIdOrderByScenarioCodeAsc(participantId);
        Map<String, SurveyResponse> byCode = new HashMap<>();
        for (SurveyResponse response : responses) {
            byCode.put(response.getScenarioCode(), response);
        }

        double a = score(byCode, "A");
        double b = score(byCode, "B");
        double c = score(byCode, "C");
        double d = score(byCode, "D");

        double motivationEffect = round(((a + b) / 2.0) - ((c + d) / 2.0));
        double opportunityEffect = round(((a + c) / 2.0) - ((b + d) / 2.0));
        double combinedEffect = round(a - d);
        double averageIntention = round((a + b + c + d) / 4.0);
        double averageJustification = round(responses.stream().mapToDouble(SurveyResponse::getJustificationScore).average().orElse(0.0));

        String profileType;
        String interpretation;
        String mainSwitch;
        String preventionTip;

        if (averageIntention <= 2.0 && averageJustification <= 2.5) {
            profileType = "안정형";
            mainSwitch = "상황 변화에도 낮은 우회 의향";
            interpretation = "대부분의 상황에서 보안절차를 유지하려는 경향이 비교적 강하게 나타났습니다.";
            preventionTip = "현재처럼 공식 승인 절차와 보안 공유 경로를 우선하는 습관을 유지하는 것이 좋습니다.";
        } else if (averageJustification >= 3.8) {
            profileType = "정당화 민감형";
            mainSwitch = "업무 목적 자기합리화";
            interpretation = "직접적인 악의보다 ‘업무 목적이면 괜찮다’는 해석이 판단에 영향을 줄 수 있습니다.";
            preventionTip = "업무 목적이라도 개인 이메일·개인 클라우드 사용은 책임 문제로 이어질 수 있으므로 공식 대체 경로를 먼저 확인하는 것이 안전합니다.";
        } else if (combinedEffect >= 2.0 && a >= 3.5) {
            profileType = "고위험 반응형";
            mainSwitch = "업무 압박과 쉬운 기회의 결합";
            interpretation = "업무 압박과 쉬운 반출 기회가 동시에 주어질 때 판단이 크게 흔들릴 수 있습니다.";
            preventionTip = "마감 상황에서는 임시 접근 권한, 보안 공유폴더, 일정 조정 요청을 먼저 사용하는 것이 위험을 낮춥니다.";
        } else if (motivationEffect >= opportunityEffect) {
            profileType = "동기 민감형";
            mainSwitch = "마감 압박과 업무 필요성";
            interpretation = "자료를 쉽게 옮길 수 있는지보다 마감 압박과 업무 필요성이 판단에 더 큰 영향을 준 것으로 보입니다.";
            preventionTip = "업무 압박이 커질수록 개인 채널보다 팀장에게 일정 조정이나 임시 권한 요청을 먼저 하는 것이 안전합니다.";
        } else {
            profileType = "기회 민감형";
            mainSwitch = "쉬운 우회 경로와 낮은 적발 가능성";
            interpretation = "업무 압박 자체보다 개인 이메일·클라우드처럼 쉬운 우회 경로가 보일 때 판단이 더 흔들릴 수 있습니다.";
            preventionTip = "편리한 우회 경로가 보일수록 공식 공유 경로를 확인하고, 개인 저장소 사용을 피하는 것이 좋습니다.";
        }

        return new ParticipantFeedback(
                profileType,
                interpretation,
                motivationEffect,
                opportunityEffect,
                combinedEffect,
                averageJustification,
                mainSwitch,
                preventionTip
        );
    }

    private double score(Map<String, SurveyResponse> byCode, String code) {
        SurveyResponse response = byCode.get(code);
        return response == null ? 0.0 : response.getIntentionScore();
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
