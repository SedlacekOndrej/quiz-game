package com.sedlacek.quiz.controller;

import com.sedlacek.quiz.dto.PlayingResponseDto;
import com.sedlacek.quiz.dto.QuestionsDto;
import com.sedlacek.quiz.dto.StatesAndAnswersDto;
import com.sedlacek.quiz.model.States;
import com.sedlacek.quiz.service.CapitalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<QuestionsDto> getQuestions(@PathVariable (name = "continent") String chosenContinent) {
        Map<String, String> continent;

        switch (chosenContinent) {
            case "europe" -> continent = States.Europe;
            case "asia" -> continent = States.AsiaAndOceania;
            case "america" -> continent = States.NorthAndSouthAmerica;
            case "africa" -> continent = States.Africa;
            default -> continent = new HashMap<>();
        }

        return capitalService.getQuestions(continent);
    }

    @PostMapping("/submit")
    public ResponseEntity<PlayingResponseDto> submitAnswers(@RequestBody StatesAndAnswersDto statesAndAnswers) {
        return capitalService.getResults(statesAndAnswers);
    }
}
