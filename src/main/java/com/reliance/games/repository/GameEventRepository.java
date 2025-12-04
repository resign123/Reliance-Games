package com.reliance.games.repository;

import com.reliance.games.entity.GameEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GameEventRepository extends JpaRepository<GameEvent, Long> {
    @Query("SELECT e FROM GameEvent e WHERE e.startTime <= :now AND e.endTime >= :now")
    List<GameEvent> findAvailableEvents(LocalDateTime now);
}

