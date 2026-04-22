package com.sps.repository;

import com.sps.model.GameRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRoundRepository extends JpaRepository<GameRound, Long> {
    List<GameRound> findByGameIdOrderByRoundNumberAsc(Long gameId);
}
