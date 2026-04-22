package com.sps.repository;

import com.sps.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("SELECT g FROM Game g ORDER BY g.createdAt DESC")
    List<Game> findAllOrderByCreatedAtDesc();
}
