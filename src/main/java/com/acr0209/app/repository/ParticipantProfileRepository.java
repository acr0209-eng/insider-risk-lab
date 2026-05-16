package com.acr0209.app.repository;

import com.acr0209.app.domain.ParticipantProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ParticipantProfileRepository extends JpaRepository<ParticipantProfile, Long> {

    @Query("select avg(p.awarenessScore) from ParticipantProfile p")
    Double averageAwarenessScore();
}
