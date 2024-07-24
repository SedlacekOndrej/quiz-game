package com.sedlacek.quiz.repository;

import com.sedlacek.quiz.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findAllByOrderByScoreDescGameTimeAsc();
}
