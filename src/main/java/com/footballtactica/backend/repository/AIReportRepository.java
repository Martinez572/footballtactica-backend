package com.footballtactica.backend.repository;

import com.footballtactica.backend.entity.AIReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface AIReportRepository extends JpaRepository<AIReport, UUID> {
    List<AIReport> findByUserId(UUID userId);
    List<AIReport> findByPlayerId(UUID playerId);
}