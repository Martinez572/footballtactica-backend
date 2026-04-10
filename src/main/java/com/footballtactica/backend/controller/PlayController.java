package com.footballtactica.backend.controller;

import com.footballtactica.backend.entity.Play;
import com.footballtactica.backend.service.PlayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/plays")
@CrossOrigin(origins = "*")
public class PlayController {

    private final PlayService playService;

    public PlayController(PlayService playService) {
        this.playService = playService;
    }

    @GetMapping("/tactic/{tacticId}")
    public ResponseEntity<List<Play>> getPlaysByTactic(@PathVariable UUID tacticId) {
        return ResponseEntity.ok(playService.getPlaysByTactic(tacticId));
    }

    @GetMapping("/tactic/{tacticId}/category/{category}")
    public ResponseEntity<List<Play>> getPlaysByCategory(
            @PathVariable UUID tacticId,
            @PathVariable String category) {
        return ResponseEntity.ok(playService.getPlaysByCategory(tacticId, category));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Play> getPlayById(@PathVariable UUID id) {
        return playService.getPlayById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Play> createPlay(@RequestBody Play play) {
        Play newPlay = playService.createPlay(play);
        return ResponseEntity.ok(playService.savePlay(newPlay));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Play> updatePlay(@PathVariable UUID id, @RequestBody Play play) {
        return ResponseEntity.ok(playService.updatePlay(id, play));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlay(@PathVariable UUID id) {
        playService.deletePlay(id);
        return ResponseEntity.noContent().build();
    }
}