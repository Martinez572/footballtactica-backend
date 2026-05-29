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

    public AIReport generatePlayerReport(UUID userId, UUID playerId, String playerName, String position, String description) {
        String analysis = geminiService.analyzePlayer(playerName, position, description);
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

    public AIReport generateTacticReport(UUID userId, String tacticName, String formation, String description) {
        String analysis = geminiService.analyzeTactic(tacticName, formation, description);
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

    public AIReport generateVideoReport(UUID userId, String videoDescription, String focusPlayer, String matchContext) {
        String analysis = geminiService.analyzeVideo(videoDescription, focusPlayer, matchContext);
        String summary = analysis.length() > 200 ? analysis.substring(0, 200) + "..." : analysis;
        AIReport report = AIReport.builder()
                .userId(userId)
                .analysisType("VIDEO")
                .content(analysis)
                .summary(summary)
                .createdAt(LocalDateTime.now())
                .build();
        return aiReportRepository.save(report);
    }

    public AIReport generateVideoFileReport(UUID userId, byte[] videoBytes, String mimeType, String userPrompt) {
    String analysis = geminiService.analyzeVideoFile(videoBytes, mimeType, userPrompt);
    String summary = analysis.length() > 200 ? analysis.substring(0, 200) + "..." : analysis;
    AIReport report = AIReport.builder()
            .userId(userId)
            .analysisType("VIDEO")
            .content(analysis)
            .summary(summary)
            .createdAt(java.time.LocalDateTime.now())
            .build();
    return aiReportRepository.save(report);
    }

    public AIReport generateComparisonReport(UUID userId, String player1, String player2, String position) {
        String analysis = geminiService.comparePlayersAnalysis(player1, player2, position);
        String summary = analysis.length() > 200 ? analysis.substring(0, 200) + "..." : analysis;
        AIReport report = AIReport.builder()
                .userId(userId)
                .analysisType("COMPARISON")
                .content(analysis)
                .summary(summary)
                .createdAt(LocalDateTime.now())
                .build();
        return aiReportRepository.save(report);
    }
    public AIReport generateVideoFramesReport(UUID userId, List<String> frames, String userPrompt) {
    String analysis = geminiService.analyzeFrames(frames, userPrompt);
    String summary = analysis.length() > 200 ? analysis.substring(0, 200) + "..." : analysis;
    AIReport report = AIReport.builder()
            .userId(userId)
            .analysisType("VIDEO")
            .content(analysis)
            .summary(summary)
            .createdAt(LocalDateTime.now())
            .build();
    return aiReportRepository.save(report);
}
}