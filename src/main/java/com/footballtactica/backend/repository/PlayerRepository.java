package com.footballtactica.backend.repository;

import com.footballtactica.backend.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface PlayerRepository extends JpaRepository<Player, UUID> {

    List<Player> findByUserId(UUID userId);

    boolean existsByNumberAndUserId(Integer number, UUID userId);
}