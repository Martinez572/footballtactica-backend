package com.footballtactica.backend.service;

import com.footballtactica.backend.entity.Tactic;
import com.footballtactica.backend.entity.TacticState;
import com.footballtactica.backend.repository.TacticRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TacticService {

    private final TacticRepository tacticRepository;

    public TacticService(TacticRepository tacticRepository) {
        this.tacticRepository = tacticRepository;
    }

    public List<Tactic> getAllTactics(UUID userId) {
        return tacticRepository.findByUserId(userId);
    }

    public List<Tactic> getTacticsByState(UUID userId, TacticState state) {
        return tacticRepository.findByUserIdAndState(userId, state);
    }

    public Optional<Tactic> getTacticById(UUID id) {
        return tacticRepository.findById(id);
    }

    public Tactic createTactic(Tactic tactic) {
        Tactic newTactic = Tactic.builder()
                .userId(tactic.getUserId())
                .name(tactic.getName())
                .description(tactic.getDescription())
                .formation(tactic.getFormation())
                .state(tactic.getState() != null ? tactic.getState() : TacticState.ATTACK)
                .data(tactic.getData())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return tacticRepository.save(newTactic);
    }

    public Tactic updateTactic(UUID id, Tactic tactic) {
        return tacticRepository.findById(id).map(existing -> {
            existing.setName(tactic.getName());
            existing.setDescription(tactic.getDescription());
            existing.setFormation(tactic.getFormation());
            existing.setState(tactic.getState());
            existing.setData(tactic.getData());
            existing.setUpdatedAt(LocalDateTime.now());
            return tacticRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Tactic not found"));
    }

    public void deleteTactic(UUID id) {
        tacticRepository.deleteById(id);
    }
}