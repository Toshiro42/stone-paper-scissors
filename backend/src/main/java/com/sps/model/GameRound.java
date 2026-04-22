package com.sps.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "game_rounds")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameRound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(nullable = false)
    private int roundNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Choice player1Choice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Choice player2Choice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoundResult result;

    @Column(nullable = false)
    private LocalDateTime playedAt = LocalDateTime.now();

    public enum Choice {
        STONE, PAPER, SCISSORS
    }

    public enum RoundResult {
        PLAYER1_WINS, PLAYER2_WINS, TIE
    }

    /**
     * Determine winner of a single round.
     * Stone beats Scissors, Scissors beats Paper, Paper beats Stone.
     */
    public static RoundResult determineWinner(Choice p1, Choice p2) {
        if (p1 == p2) return RoundResult.TIE;

        boolean p1Wins = (p1 == Choice.STONE    && p2 == Choice.SCISSORS) ||
                         (p1 == Choice.SCISSORS  && p2 == Choice.PAPER)    ||
                         (p1 == Choice.PAPER     && p2 == Choice.STONE);

        return p1Wins ? RoundResult.PLAYER1_WINS : RoundResult.PLAYER2_WINS;
    }
}
