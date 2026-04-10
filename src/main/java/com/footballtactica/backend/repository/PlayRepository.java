package com.footballtactica.backend.repository;

import com.footballtactica.backend.entity.Play;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface PlayRepository extends JpaRepository<Play, UUID> {
    List<Play> findByTacticIdOrderByOrderAsc(UUID tacticId);
    List<Play> findByTacticIdAndCategory(UUID tacticId, String category);
}