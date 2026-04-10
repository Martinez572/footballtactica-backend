package com.footballtactica.backend.repository;

import com.footballtactica.backend.entity.Tactic;
import com.footballtactica.backend.entity.TacticState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface TacticRepository extends JpaRepository<Tactic, UUID> {

    List<Tactic> findByUserId(UUID userId);

    List<Tactic> findByUserIdAndState(UUID userId, TacticState state);
}