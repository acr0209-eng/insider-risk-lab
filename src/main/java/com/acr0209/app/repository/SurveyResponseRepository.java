package com.acr0209.app.repository;

import com.acr0209.app.domain.SurveyResponse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, Long> {

    @Query("""
            select r.scenarioCode, count(r), avg(r.intentionScore), avg(r.justificationScore), avg(r.riskScore)
            from SurveyResponse r
            group by r.scenarioCode
            order by r.scenarioCode
            """)
    List<Object[]> summarizeByScenario();

    @Query("""
            select r.scenarioCode, r.actionChoice, count(r)
            from SurveyResponse r
            group by r.scenarioCode, r.actionChoice
            order by r.scenarioCode, r.actionChoice
            """)
    List<Object[]> countActionChoicesByScenario();

    @Query("select count(distinct r.participantId) from SurveyResponse r")
    long countDistinctParticipants();

    List<SurveyResponse> findByParticipantIdOrderByScenarioCodeAsc(String participantId);

    long countByTooFastTrue();

    long countByStraightLinedTrue();

    @Query("select avg(r.durationSeconds) from SurveyResponse r")
    Double averageDurationSeconds();
}
