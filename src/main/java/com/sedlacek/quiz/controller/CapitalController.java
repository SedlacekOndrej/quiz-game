package com.sedlacek.quiz.controller;

import com.sedlacek.quiz.dto.PlayingResponseDto;
import com.sedlacek.quiz.dto.QuestionsDto;
import com.sedlacek.quiz.dto.StatesAndAnswersDto;
import com.sedlacek.quiz.model.GameType;
import com.sedlacek.quiz.service.CapitalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/capitals")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@SuppressWarnings("unused")
public class CapitalController {
    private final CapitalService capitalService;

    public CapitalController(CapitalService capitalService) {
        this.capitalService = capitalService;
    }

    @GetMapping("/{continent}")
    public ResponseEntity<QuestionsDto> getQuestions(@PathVariable (name = "continent") String continent,
                                                     @RequestParam (name = "type") String gameType) {
        return capitalService.getQuestions(continent, GameType.valueOf(gameType.toUpperCase()));
    }

    @PostMapping("/submit")
    public ResponseEntity<PlayingResponseDto> submitAnswers(@RequestBody StatesAndAnswersDto statesAndAnswers) {
        return capitalService.getResults(statesAndAnswers);
    }
}
