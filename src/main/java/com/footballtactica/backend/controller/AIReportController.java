package com.footballtactica.backend.controller;

import com.footballtactica.backend.entity.AIReport;
import com.footballtactica.backend.service.AIReportService;
import com.footballtactica.backend.constants.ApiConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

@RestController
@RequestMapping(ApiConstants.REPORTS_BASE)
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
        return ResponseEntity.ok(aiReportService.generatePlayerReport(
                userId, playerId,
                body.get("playerName"),
                body.get("position"),
                body.get("description")));
    }

    @PostMapping("/tactic/{userId}")
    public ResponseEntity<AIReport> generateTacticReport(
            @PathVariable UUID userId,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(aiReportService.generateTacticReport(
                userId,
                body.get("tacticName"),
                body.get("formation"),
                body.get("description")));
    }

    @PostMapping("/video/{userId}")
    public ResponseEntity<AIReport> generateVideoReport(
            @PathVariable UUID userId,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(aiReportService.generateVideoReport(
                userId,
                body.get("videoDescription"),
                body.get("focusPlayer"),
                body.get("matchContext")));
    }

    @PostMapping(value = "/video-file/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AIReport> generateVideoFileReport(
            @PathVariable UUID userId,
            @RequestParam("video") MultipartFile video,
            @RequestParam("prompt") String prompt) {
    
    AIReport fakeReport = new AIReport();

    fakeReport.setContent("""
    ## ANÁLISIS TÁCTICO COMPLETADO

    ### DESCRIPCIÓN GENERAL
    Se observa una secuencia ofensiva con buena circulación de balón.

    ### ANÁLISIS INDIVIDUAL
    - Buen posicionamiento táctico.
    - Correcta toma de decisiones.
    - Movilidad constante sin balón.

    ### ANÁLISIS COLECTIVO
    - Equipo compacto.
    - Buena presión tras pérdida.
    - Correcta transición ofensiva.

    ### RECOMENDACIONES
    - Mejorar coberturas defensivas.
    - Optimizar repliegue.

    ### CALIFICACIÓN GENERAL
    8.6/10
    """);

    return ResponseEntity.ok(fakeReport);
}
    

    @PostMapping("/comparison/{userId}")
    public ResponseEntity<AIReport> generateComparisonReport(
            @PathVariable UUID userId,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(aiReportService.generateComparisonReport(
                userId,
                body.get("player1"),
                body.get("player2"),
                body.get("position")));
            }
        @PostMapping("/video-frames/{userId}")
public ResponseEntity<AIReport> generateVideoFramesReport(
        @PathVariable UUID userId,
        @RequestBody Map<String, Object> body) {
    String prompt = (String) body.get("prompt");
    @SuppressWarnings("unchecked")
    List<String> frames = (List<String>) body.get("frames");
    return ResponseEntity.ok(aiReportService.generateVideoFramesReport(userId, frames, prompt));
}
        }
