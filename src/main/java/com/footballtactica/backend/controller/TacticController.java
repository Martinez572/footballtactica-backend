package com.footballtactica.backend.controller;

import com.footballtactica.backend.constants.ApiConstants;
import com.footballtactica.backend.entity.Tactic;
import com.footballtactica.backend.entity.TacticState;
import com.footballtactica.backend.service.TacticService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import com.footballtactica.backend.constants.ApiConstants;

@RestController
@RequestMapping(ApiConstants.TACTICS_BASE)
@CrossOrigin(origins = "*")
public class TacticController {

    private final TacticService tacticService;

    public TacticController(TacticService tacticService) {
        this.tacticService = tacticService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Tactic>> getTacticsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(tacticService.getAllTactics(userId));
    }

    @GetMapping("/user/{userId}/state/{state}")
    public ResponseEntity<List<Tactic>> getTacticsByState(
            @PathVariable UUID userId,
            @PathVariable TacticState state) {
        return ResponseEntity.ok(tacticService.getTacticsByState(userId, state));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tactic> getTacticById(@PathVariable UUID id) {
        return tacticService.getTacticById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Tactic> createTactic(@RequestBody Tactic tactic) {
        return ResponseEntity.ok(tacticService.createTactic(tactic));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tactic> updateTactic(@PathVariable UUID id, @RequestBody Tactic tactic) {
        return ResponseEntity.ok(tacticService.updateTactic(id, tactic));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTactic(@PathVariable UUID id) {
        tacticService.deleteTactic(id);
        return ResponseEntity.noContent().build();
    }
}