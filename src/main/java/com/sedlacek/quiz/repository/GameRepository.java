package com.sedlacek.quiz.repository;

import com.sedlacek.quiz.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findAllByOrderByScoreDescGameTimeAsc();
}
