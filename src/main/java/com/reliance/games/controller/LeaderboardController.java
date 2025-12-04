package com.reliance.games.controller;

import com.reliance.games.entity.Score;
import com.reliance.games.repository.PlayerRepository;
import com.reliance.games.repository.ScoreRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    private final ScoreRepository scoreRepository;
    private final PlayerRepository playerRepository;

    public LeaderboardController(ScoreRepository scoreRepository, PlayerRepository playerRepository) {
        this.scoreRepository = scoreRepository;
        this.playerRepository = playerRepository;
    }

    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitScore(@RequestBody Map<String, Object> request) {
        Long playerId = Long.valueOf(request.get("playerId").toString());
        Long gameId = Long.valueOf(request.get("gameId").toString());
        Long score = Long.valueOf(request.get("score").toString());
        LocalDateTime timestamp = request.containsKey("timestamp") 
            ? LocalDateTime.parse(request.get("timestamp").toString())
            : LocalDateTime.now();

        Optional<com.reliance.games.entity.Player> playerOpt = playerRepository.findById(playerId);
        if (playerOpt.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Player not found");
            return ResponseEntity.badRequest().body(response);
        }

        Score scoreEntity = new Score();
        scoreEntity.setPlayer(playerOpt.get());
        scoreEntity.setGameId(gameId);
        scoreEntity.setScore(score);
        scoreEntity.setTimestamp(timestamp);
        scoreRepository.save(scoreEntity);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Score submitted successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/top/{limit}")
    public ResponseEntity<Map<String, Object>> getTopPlayers(
            @PathVariable Integer limit,
            @RequestParam(required = false) Long gameId) {
        
        List<Score> scores;
        if (gameId != null) {
            scores = scoreRepository.findTopByGameIdOrderByScoreDesc(gameId);
        } else {
            scores = scoreRepository.findAll().stream()
                    .sorted((a, b) -> Long.compare(b.getScore(), a.getScore()))
                    .collect(Collectors.toList());
        }

        List<Map<String, Object>> topPlayers = scores.stream()
                .limit(limit)
                .map(score -> {
                    Map<String, Object> playerData = new HashMap<>();
                    playerData.put("playerId", score.getPlayer().getId());
                    playerData.put("userName", score.getPlayer().getUserName());
                    playerData.put("score", score.getScore());
                    playerData.put("gameId", score.getGameId());
                    playerData.put("timestamp", score.getTimestamp());
                    return playerData;
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("topPlayers", topPlayers);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/top/{limit}/country")
    public ResponseEntity<Map<String, Object>> getTopPlayersByCountry(
            @PathVariable Integer limit,
            @RequestParam Long gameId,
            @RequestParam String country) {
        
        List<Score> scores = scoreRepository.findTopByGameIdAndCountryOrderByScoreDesc(gameId, country);

        List<Map<String, Object>> topPlayers = scores.stream()
                .limit(limit)
                .map(score -> {
                    Map<String, Object> playerData = new HashMap<>();
                    playerData.put("playerId", score.getPlayer().getId());
                    playerData.put("userName", score.getPlayer().getUserName());
                    playerData.put("score", score.getScore());
                    playerData.put("gameId", score.getGameId());
                    playerData.put("country", country);
                    playerData.put("timestamp", score.getTimestamp());
                    return playerData;
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("topPlayers", topPlayers);
        return ResponseEntity.ok(response);
    }
}

