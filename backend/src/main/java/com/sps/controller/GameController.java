package com.sps.controller;

import com.sps.dto.GameDto;
import com.sps.service.GameService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "*")
public class GameController {

    @Autowired
    private GameService gameService;

    /**
     * POST /api/games
     * Create a new game with player names.
     */
    @PostMapping
    public ResponseEntity<GameDto.GameResponse> createGame(
            @Valid @RequestBody GameDto.CreateGameRequest request) {
        GameDto.GameResponse game = gameService.createGame(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(game);
    }

    /**
     * POST /api/games/{id}/rounds
     * Play a round in an existing game.
     */
    @PostMapping("/{id}/rounds")
    public ResponseEntity<GameDto.PlayRoundResponse> playRound(
            @PathVariable Long id,
            @Valid @RequestBody GameDto.PlayRoundRequest request) {
        GameDto.PlayRoundResponse response = gameService.playRound(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/games/{id}
     * Get a single game with all rounds.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GameDto.GameResponse> getGame(@PathVariable Long id) {
        return ResponseEntity.ok(gameService.getGame(id));
    }

    /**
     * GET /api/games
     * Get all games, newest first.
     */
    @GetMapping
    public ResponseEntity<List<GameDto.GameResponse>> getAllGames() {
        return ResponseEntity.ok(gameService.getAllGames());
    }
}
