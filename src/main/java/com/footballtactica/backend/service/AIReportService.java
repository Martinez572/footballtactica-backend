package com.footballtactica.backend.service;

import com.footballtactica.backend.entity.AIReport;
import com.footballtactica.backend.repository.AIReportRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AIReportService {

    private final AIReportRepository aiReportRepository;
    private final GeminiService geminiService;

    public AIReportService(AIReportRepository aiReportRepository, GeminiService geminiService) {
        this.aiReportRepository = aiReportRepository;
        this.geminiService = geminiService;
    }

    public List<AIReport> getReportsByUser(UUID userId) {
        return aiReportRepository.findByUserId(userId);
    }

    public List<AIReport> getReportsByPlayer(UUID playerId) {
        return aiReportRepository.findByPlayerId(playerId);
    }

    public AIReport generatePlayerReport(UUID userId, UUID playerId, String playerDescription) {
        String prompt = "Eres un analista de fútbol profesional. Analiza el siguiente jugador y genera un reporte táctico detallado en español:\n\n" + playerDescription + "\n\nIncluye: fortalezas, debilidades, posición ideal, y recomendaciones de mejora.";

        String analysis = geminiService.analyze(prompt);

        String summary = analysis.length() > 200 ? analysis.substring(0, 200) + "..." : analysis;

        AIReport report = AIReport.builder()
                .userId(userId)
                .playerId(playerId)
                .analysisType("PLAYER")
                .content(analysis)
                .summary(summary)
                .createdAt(LocalDateTime.now())
                .build();

        return aiReportRepository.save(report);
    }

    public AIReport generateTacticReport(UUID userId, String tacticDescription) {
        String prompt = "Eres un analista táctico de fútbol profesional. Analiza la siguiente táctica y genera un reporte detallado en español:\n\n" + tacticDescription + "\n\nIncluye: efectividad, vulnerabilidades, situaciones ideales de uso y recomendaciones.";

        String analysis = geminiService.analyze(prompt);

        String summary = analysis.length() > 200 ? analysis.substring(0, 200) + "..." : analysis;

        AIReport report = AIReport.builder()
                .userId(userId)
                .analysisType("TACTIC")
                .content(analysis)
                .summary(summary)
                .createdAt(LocalDateTime.now())
                .build();

        return aiReportRepository.save(report);
    }
}