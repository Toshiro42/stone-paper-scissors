package com.sps.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "games")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String player1Name;

    @Column(nullable = false)
    private String player2Name;

    @Column(nullable = false)
    private int player1Score = 0;

    @Column(nullable = false)
    private int player2Score = 0;

    @Column(nullable = false)
    private int tieCount = 0;

    @Column(nullable = false)
    private int totalRounds = 6;

    @Column(nullable = false)
    private int completedRounds = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameStatus status = GameStatus.IN_PROGRESS;

    private String winner; // null = tie, player name otherwise

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime completedAt;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("roundNumber ASC")
    private List<GameRound> rounds = new ArrayList<>();

    public enum GameStatus {
        IN_PROGRESS, COMPLETED
    }
}
