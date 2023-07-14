package com.sedlacek.quiz.controller;

import com.sedlacek.quiz.dto.*;
import com.sedlacek.quiz.model.GameType;
import com.sedlacek.quiz.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SuppressWarnings("unused")
@RequestMapping("/api/game")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class GameController {

    private final GameService gameService;


    public GameController(GameService gameService) {
        this.gameService = gameService;
    }


    @GetMapping("/{continent}")
    public ResponseEntity<QuestionsDto> getQuestions(@PathVariable (name = "continent") String continent,
                                                     @RequestParam (name = "type") String gameType) {
        return gameService.getQuestions(continent, GameType.valueOf(gameType.toUpperCase()));
    }

    @PostMapping("/submit")
    public ResponseEntity<PlayingResponseDto> submitAnswers(@RequestBody QuestionsAndAnswersDto statesAndAnswers) {
        return gameService.submitAnswers(statesAndAnswers);
    }

    @GetMapping("/history")
    public ResponseEntity<List<GameHistoryDto>> getAllGamesHistory() {
        return gameService.getAllGamesHistory();
    }

    @GetMapping("/history/{id}")
    public ResponseEntity<List<GameDto>> getUserGamesHistory(@PathVariable (name = "id") long userId) {
        return gameService.getUserGamesHistory(userId);
    }
}
