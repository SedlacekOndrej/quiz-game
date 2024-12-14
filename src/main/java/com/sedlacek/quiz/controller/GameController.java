package com.sedlacek.quiz.controller;

import com.sedlacek.quiz.dto.GameDto;
import com.sedlacek.quiz.entity.EntityBase;
import com.sedlacek.quiz.entity.Game;
import com.sedlacek.quiz.exception.ResourceNotFoundException;
import com.sedlacek.quiz.model.Continent;
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


    @PostMapping("/{continent}")
    public ResponseEntity<GameDto> createGame(@PathVariable(name = "continent") String continent,
                                              @RequestParam(name = "userId") long userId,
                                              @RequestParam(name = "type") String gameType,
                                              @RequestParam(name = "questions", required = false,
                                                      defaultValue = "10") int numberOfQuestions)
            throws ResourceNotFoundException {
        Game game = gameService.createGame(Continent.valueOf(continent.toUpperCase()), userId,
                GameType.valueOf(gameType.toUpperCase()), numberOfQuestions);
        GameDto gameDto = EntityBase.convert(game, GameDto.class);
        return ResponseEntity.ok(gameDto);
    }

    @PostMapping
    public ResponseEntity<GameDto> evaluateGame(@RequestBody GameDto updatedGame) throws ResourceNotFoundException {
        Game game = gameService.evaluateGame(updatedGame);
        GameDto gameDto = EntityBase.convert(game, GameDto.class);
        return ResponseEntity.ok(gameDto);
    }

    @GetMapping("/history")
    public ResponseEntity<List<GameDto>> getAllGamesHistory() {
        List<Game> games = gameService.getAllGamesHistory();
        List<GameDto> gamesDto = EntityBase.convertAll(games, GameDto.class);
        return ResponseEntity.ok(gamesDto);
    }
}
