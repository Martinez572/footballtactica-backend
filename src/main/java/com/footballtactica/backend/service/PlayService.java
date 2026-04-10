package com.footballtactica.backend.service;

import com.footballtactica.backend.entity.Play;
import com.footballtactica.backend.repository.PlayRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayService {

    private final PlayRepository playRepository;

    public PlayService(PlayRepository playRepository) {
        this.playRepository = playRepository;
    }

    public List<Play> getPlaysByTactic(UUID tacticId) {
        return playRepository.findByTacticIdOrderByOrderAsc(tacticId);
    }

    public List<Play> getPlaysByCategory(UUID tacticId, String category) {
        return playRepository.findByTacticIdAndCategory(tacticId, category);
    }

    public Optional<Play> getPlayById(UUID id) {
        return playRepository.findById(id);
    }

    public Play createPlay(Play play) {
        return Play.builder()
                .tacticId(play.getTacticId())
                .name(play.getName())
                .category(play.getCategory())
                .order(play.getOrder())
                .data(play.getData())
                .build();
    }

    public Play savePlay(Play play) {
        return playRepository.save(play);
    }

    public Play updatePlay(UUID id, Play play) {
        return playRepository.findById(id).map(existing -> {
            existing.setName(play.getName());
            existing.setCategory(play.getCategory());
            existing.setOrder(play.getOrder());
            existing.setData(play.getData());
            return playRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Jugada no encontrada"));
    }

    public void deletePlay(UUID id) {
        playRepository.deleteById(id);
    }
}