package com.sps.dto;

import com.sps.model.Game;
import com.sps.model.GameRound;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class GameDto {

    // ─── Request DTOs ─────────────────────────────────────

    @Data
    public static class CreateGameRequest {
        @NotBlank(message = "Player 1 name is required")
        private String player1Name;

        @NotBlank(message = "Player 2 name is required")
        private String player2Name;
    }

    @Data
    public static class PlayRoundRequest {
        @NotNull(message = "Player 1 choice is required")
        private GameRound.Choice player1Choice;

        @NotNull(message = "Player 2 choice is required")
        private GameRound.Choice player2Choice;
    }

    // ─── Response DTOs ────────────────────────────────────

    @Data
    public static class RoundResponse {
        private Long id;
        private int roundNumber;
        private GameRound.Choice player1Choice;
        private GameRound.Choice player2Choice;
        private GameRound.RoundResult result;
        private String resultLabel;
        private LocalDateTime playedAt;

        public static RoundResponse from(GameRound round, String p1Name, String p2Name) {
            RoundResponse r = new RoundResponse();
            r.id = round.getId();
            r.roundNumber = round.getRoundNumber();
            r.player1Choice = round.getPlayer1Choice();
            r.player2Choice = round.getPlayer2Choice();
            r.result = round.getResult();
            r.playedAt = round.getPlayedAt();
            switch (round.getResult()) {
                case PLAYER1_WINS -> r.resultLabel = p1Name + " wins!";
                case PLAYER2_WINS -> r.resultLabel = p2Name + " wins!";
                case TIE          -> r.resultLabel = "It's a tie!";
            }
            return r;
        }
    }

    @Data
    public static class GameResponse {
        private Long id;
        private String player1Name;
        private String player2Name;
        private int player1Score;
        private int player2Score;
        private int tieCount;
        private int totalRounds;
        private int completedRounds;
        private Game.GameStatus status;
        private String winner;
        private LocalDateTime createdAt;
        private LocalDateTime completedAt;
        private List<RoundResponse> rounds;

        public static GameResponse from(Game game) {
            GameResponse gr = new GameResponse();
            gr.id = game.getId();
            gr.player1Name = game.getPlayer1Name();
            gr.player2Name = game.getPlayer2Name();
            gr.player1Score = game.getPlayer1Score();
            gr.player2Score = game.getPlayer2Score();
            gr.tieCount = game.getTieCount();
            gr.totalRounds = game.getTotalRounds();
            gr.completedRounds = game.getCompletedRounds();
            gr.status = game.getStatus();
            gr.winner = game.getWinner();
            gr.createdAt = game.getCreatedAt();
            gr.completedAt = game.getCompletedAt();
            gr.rounds = game.getRounds().stream()
                    .map(r -> RoundResponse.from(r, game.getPlayer1Name(), game.getPlayer2Name()))
                    .collect(Collectors.toList());
            return gr;
        }
    }

    @Data
    public static class PlayRoundResponse {
        private RoundResponse round;
        private GameResponse game;
    }
}
