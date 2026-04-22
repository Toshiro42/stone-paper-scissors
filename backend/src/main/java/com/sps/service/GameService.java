package com.sps.service;

import com.sps.dto.GameDto;
import com.sps.model.Game;
import com.sps.model.GameRound;
import com.sps.repository.GameRepository;
import com.sps.repository.GameRoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameRoundRepository gameRoundRepository;

    /**
     * Create a new game with two player names.
     */
    public GameDto.GameResponse createGame(GameDto.CreateGameRequest request) {
        Game game = new Game();
        game.setPlayer1Name(request.getPlayer1Name().trim());
        game.setPlayer2Name(request.getPlayer2Name().trim());
        game = gameRepository.save(game);
        return GameDto.GameResponse.from(game);
    }

    /**
     * Play a single round of the game.
     */
    public GameDto.PlayRoundResponse playRound(Long gameId, GameDto.PlayRoundRequest request) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found: " + gameId));

        if (game.getStatus() == Game.GameStatus.COMPLETED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game is already completed");
        }

        if (game.getCompletedRounds() >= game.getTotalRounds()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "All rounds have been played");
        }

        // Determine round result
        int roundNumber = game.getCompletedRounds() + 1;
        GameRound.RoundResult result = GameRound.determineWinner(
                request.getPlayer1Choice(), request.getPlayer2Choice());

        // Save round
        GameRound round = new GameRound();
        round.setGame(game);
        round.setRoundNumber(roundNumber);
        round.setPlayer1Choice(request.getPlayer1Choice());
        round.setPlayer2Choice(request.getPlayer2Choice());
        round.setResult(result);
        round = gameRoundRepository.save(round);

        // Update scores
        switch (result) {
            case PLAYER1_WINS -> game.setPlayer1Score(game.getPlayer1Score() + 1);
            case PLAYER2_WINS -> game.setPlayer2Score(game.getPlayer2Score() + 1);
            case TIE          -> game.setTieCount(game.getTieCount() + 1);
        }
        game.setCompletedRounds(roundNumber);

        // Check if game is over
        if (roundNumber >= game.getTotalRounds()) {
            game.setStatus(Game.GameStatus.COMPLETED);
            game.setCompletedAt(LocalDateTime.now());

            if (game.getPlayer1Score() > game.getPlayer2Score()) {
                game.setWinner(game.getPlayer1Name());
            } else if (game.getPlayer2Score() > game.getPlayer1Score()) {
                game.setWinner(game.getPlayer2Name());
            } else {
                game.setWinner("TIE");
            }
        }

        game = gameRepository.save(game);

        // Reload rounds for response
        List<GameRound> rounds = gameRoundRepository.findByGameIdOrderByRoundNumberAsc(gameId);
        game.setRounds(rounds);

        GameDto.PlayRoundResponse response = new GameDto.PlayRoundResponse();
        response.setRound(GameDto.RoundResponse.from(round, game.getPlayer1Name(), game.getPlayer2Name()));
        response.setGame(GameDto.GameResponse.from(game));
        return response;
    }

    /**
     * Get a single game by ID.
     */
    @Transactional(readOnly = true)
    public GameDto.GameResponse getGame(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found: " + gameId));
        List<GameRound> rounds = gameRoundRepository.findByGameIdOrderByRoundNumberAsc(gameId);
        game.setRounds(rounds);
        return GameDto.GameResponse.from(game);
    }

    /**
     * Get all games ordered by date descending.
     */
    @Transactional(readOnly = true)
    public List<GameDto.GameResponse> getAllGames() {
        return gameRepository.findAllOrderByCreatedAtDesc()
                .stream()
                .map(game -> {
                    List<GameRound> rounds = gameRoundRepository.findByGameIdOrderByRoundNumberAsc(game.getId());
                    game.setRounds(rounds);
                    return GameDto.GameResponse.from(game);
                })
                .collect(Collectors.toList());
    }
}
