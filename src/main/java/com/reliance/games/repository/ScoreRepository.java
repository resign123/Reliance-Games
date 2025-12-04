package com.reliance.games.repository;

import com.reliance.games.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    @Query("SELECT s FROM Score s WHERE s.gameId = :gameId ORDER BY s.score DESC")
    List<Score> findTopByGameIdOrderByScoreDesc(Long gameId);

    @Query("SELECT DISTINCT s FROM Score s JOIN s.player p JOIN PlayerProgression pp ON p.id = pp.player.id WHERE s.gameId = :gameId AND pp.country = :country ORDER BY s.score DESC")
    List<Score> findTopByGameIdAndCountryOrderByScoreDesc(Long gameId, String country);
}

