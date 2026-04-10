package com.footballtactica.backend.service;

import com.footballtactica.backend.entity.Player;
import com.footballtactica.backend.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> getAllPlayers(UUID userId) {
        return playerRepository.findByUserId(userId);
    }

    public Optional<Player> getPlayerById(UUID id) {
        return playerRepository.findById(id);
    }

    public Player createPlayer(Player player) {
        return Player.builder()
                .userId(player.getUserId())
                .name(player.getName())
                .number(player.getNumber())
                .position(player.getPosition())
                .age(player.getAge())
                .nationality(player.getNationality())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    public void deletePlayer(UUID id) {
        playerRepository.deleteById(id);
    }
}