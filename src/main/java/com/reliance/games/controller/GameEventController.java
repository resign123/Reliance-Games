package com.reliance.games.controller;

import com.reliance.games.entity.GameEvent;
import com.reliance.games.repository.GameEventRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
public class GameEventController {

    private final GameEventRepository gameEventRepository;

    public GameEventController(GameEventRepository gameEventRepository) {
        this.gameEventRepository = gameEventRepository;
    }

    @PostMapping("/schedule")
    public ResponseEntity<Map<String, Object>> scheduleEvent(@RequestBody Map<String, Object> request) {
        String name = request.get("name").toString();
        LocalDateTime startTime = LocalDateTime.parse(request.get("startTime").toString());
        LocalDateTime endTime = LocalDateTime.parse(request.get("endTime").toString());
        String configuration = request.containsKey("configuration") 
            ? request.get("configuration").toString() 
            : null;

        if (startTime.isAfter(endTime)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Start time must be before end time");
            return ResponseEntity.badRequest().body(response);
        }

        GameEvent event = new GameEvent();
        event.setName(name);
        event.setStartTime(startTime);
        event.setEndTime(endTime);
        event.setConfiguration(configuration);
        event = gameEventRepository.save(event);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("eventId", event.getId());
        response.put("message", "Event scheduled successfully");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Map<String, Object>> updateEvent(
            @PathVariable Long eventId,
            @RequestBody Map<String, Object> request) {
        
        Optional<GameEvent> eventOpt = gameEventRepository.findById(eventId);
        if (eventOpt.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Event not found");
            return ResponseEntity.badRequest().body(response);
        }

        GameEvent event = eventOpt.get();
        if (request.containsKey("name")) {
            event.setName(request.get("name").toString());
        }
        if (request.containsKey("startTime")) {
            event.setStartTime(LocalDateTime.parse(request.get("startTime").toString()));
        }
        if (request.containsKey("endTime")) {
            event.setEndTime(LocalDateTime.parse(request.get("endTime").toString()));
        }
        if (request.containsKey("configuration")) {
            event.setConfiguration(request.get("configuration").toString());
        }

        if (event.getStartTime().isAfter(event.getEndTime())) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Start time must be before end time");
            return ResponseEntity.badRequest().body(response);
        }

        gameEventRepository.save(event);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Event updated successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/available")
    public ResponseEntity<Map<String, Object>> getAvailableEvents() {
        List<GameEvent> events = gameEventRepository.findAvailableEvents(LocalDateTime.now());

        List<Map<String, Object>> eventList = events.stream()
                .map(event -> {
                    Map<String, Object> eventData = new HashMap<>();
                    eventData.put("eventId", event.getId());
                    eventData.put("name", event.getName());
                    eventData.put("startTime", event.getStartTime());
                    eventData.put("endTime", event.getEndTime());
                    eventData.put("configuration", event.getConfiguration());
                    return eventData;
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("events", eventList);
        return ResponseEntity.ok(response);
    }
}

