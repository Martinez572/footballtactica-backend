package com.footballtactica.backend.controller;

import com.footballtactica.backend.entity.AIReport;
import com.footballtactica.backend.service.AIReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class AIReportController {

    private final AIReportService aiReportService;

    public AIReportController(AIReportService aiReportService) {
        this.aiReportService = aiReportService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AIReport>> getReportsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(aiReportService.getReportsByUser(userId));
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<AIReport>> getReportsByPlayer(@PathVariable UUID playerId) {
        return ResponseEntity.ok(aiReportService.getReportsByPlayer(playerId));
    }

    @PostMapping("/player/{userId}/{playerId}")
    public ResponseEntity<AIReport> generatePlayerReport(
            @PathVariable UUID userId,
            @PathVariable UUID playerId,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(aiReportService.generatePlayerReport(userId, playerId, body.get("description")));
    }

    @PostMapping("/tactic/{userId}")
    public ResponseEntity<AIReport> generateTacticReport(
            @PathVariable UUID userId,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(aiReportService.generateTacticReport(userId, body.get("description")));
    }
}
