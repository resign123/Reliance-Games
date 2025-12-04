package com.reliance.games.repository;

import com.reliance.games.entity.PlayerProgression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerProgressionRepository extends JpaRepository<PlayerProgression, Long> {
    @Query("SELECT pp FROM PlayerProgression pp WHERE pp.player.id = :playerId")
    Optional<PlayerProgression> findByPlayerId(Long playerId);
}

