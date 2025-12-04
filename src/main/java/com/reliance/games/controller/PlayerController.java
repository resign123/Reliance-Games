package com.reliance.games.controller;

import com.reliance.games.entity.Player;
import com.reliance.games.entity.PlayerProgression;
import com.reliance.games.repository.PlayerProgressionRepository;
import com.reliance.games.repository.PlayerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    private final PlayerRepository playerRepository;
    private final PlayerProgressionRepository playerProgressionRepository;

    public PlayerController(PlayerRepository playerRepository, PlayerProgressionRepository playerProgressionRepository) {
        this.playerRepository = playerRepository;
        this.playerProgressionRepository = playerProgressionRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerPlayer(@RequestBody Map<String, String> request) {
        String deviceId = request.get("deviceId");
        String userName = request.get("userName");
        String platform = request.get("platform");

        if (deviceId == null || userName == null || platform == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Missing required fields: deviceId, userName, platform");
            return ResponseEntity.badRequest().body(response);
        }

        Optional<Player> existingPlayer = playerRepository.findByDeviceId(deviceId);
        if (existingPlayer.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Player with deviceId already exists");
            return ResponseEntity.badRequest().body(response);
        }

        Player player = new Player();
        player.setDeviceId(deviceId);
        player.setUserName(userName);
        player.setPlatform(platform);
        player.setCreationDate(LocalDateTime.now());
        player = playerRepository.save(player);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("playerId", player.getId());
        response.put("message", "Player registered successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/progression")
    public ResponseEntity<Map<String, Object>> saveProgression(@RequestBody Map<String, Object> request) {
        try {
            if (!request.containsKey("playerId") || request.get("playerId") == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Missing required field: playerId");
                return ResponseEntity.badRequest().body(response);
            }

            Long playerId;
            try {
                Object playerIdObj = request.get("playerId");
                if (playerIdObj instanceof Number) {
                    playerId = ((Number) playerIdObj).longValue();
                } else {
                    playerId = Long.valueOf(playerIdObj.toString());
                }
            } catch (NumberFormatException e) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Invalid playerId format");
                return ResponseEntity.badRequest().body(response);
            }

            Optional<Player> playerOpt = playerRepository.findById(playerId);
            if (playerOpt.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Player not found");
                return ResponseEntity.badRequest().body(response);
            }

            PlayerProgression progression = playerProgressionRepository.findByPlayerId(playerId)
                    .orElse(new PlayerProgression());
            progression.setPlayer(playerOpt.get());

            if (request.containsKey("level") && request.get("level") != null) {
                try {
                    Object levelObj = request.get("level");
                    if (levelObj instanceof Number) {
                        progression.setLevel(((Number) levelObj).intValue());
                    } else {
                        progression.setLevel(Integer.valueOf(levelObj.toString()));
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid level
                }
            }
            if (request.containsKey("rank") && request.get("rank") != null) {
                try {
                    Object rankObj = request.get("rank");
                    if (rankObj instanceof Number) {
                        progression.setRank(((Number) rankObj).intValue());
                    } else {
                        progression.setRank(Integer.valueOf(rankObj.toString()));
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid rank
                }
            }
            if (request.containsKey("gold") && request.get("gold") != null) {
                try {
                    Object goldObj = request.get("gold");
                    if (goldObj instanceof Number) {
                        progression.setGold(((Number) goldObj).longValue());
                    } else {
                        progression.setGold(Long.valueOf(goldObj.toString()));
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid gold
                }
            }
            if (request.containsKey("cash") && request.get("cash") != null) {
                try {
                    Object cashObj = request.get("cash");
                    if (cashObj instanceof Number) {
                        progression.setCash(((Number) cashObj).longValue());
                    } else {
                        progression.setCash(Long.valueOf(cashObj.toString()));
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid cash
                }
            }
            if (request.containsKey("gem") && request.get("gem") != null) {
                try {
                    Object gemObj = request.get("gem");
                    if (gemObj instanceof Number) {
                        progression.setGem(((Number) gemObj).longValue());
                    } else {
                        progression.setGem(Long.valueOf(gemObj.toString()));
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid gem
                }
            }
            if (request.containsKey("rewardsCollected") && request.get("rewardsCollected") != null) {
                progression.setRewardsCollected(request.get("rewardsCollected").toString());
            }
            if (request.containsKey("lastActiveTime") && request.get("lastActiveTime") != null) {
                try {
                    progression.setLastActiveTime(LocalDateTime.parse(request.get("lastActiveTime").toString()));
                } catch (Exception e) {
                    progression.setLastActiveTime(LocalDateTime.now());
                }
            } else {
                progression.setLastActiveTime(LocalDateTime.now());
            }
            if (request.containsKey("country") && request.get("country") != null) {
                progression.setCountry(request.get("country").toString());
            }

            playerProgressionRepository.save(progression);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Progression saved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error saving progression: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

