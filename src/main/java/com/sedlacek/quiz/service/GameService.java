package com.sedlacek.quiz.service;

import com.sedlacek.quiz.dto.GameDto;
import com.sedlacek.quiz.entity.Game;
import com.sedlacek.quiz.exception.ResourceNotFoundException;
import com.sedlacek.quiz.model.Continent;
import com.sedlacek.quiz.model.GameType;

import java.util.List;

public interface GameService {
    Game createGame(Continent continent, long userId, GameType gameType, int numberOfQuestions) throws ResourceNotFoundException;

    Game evaluateGame(GameDto gameDto) throws ResourceNotFoundException;

    /**
     * Selects all the games played from the database, converts it to DTO a returns it.
     *
     * @return list of all games played by all users
     */
    List<Game> getAllGamesHistory();
}
