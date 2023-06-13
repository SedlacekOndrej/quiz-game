package com.sedlacek.quiz.service;

import com.sedlacek.quiz.dto.AnswersDto;
import com.sedlacek.quiz.dto.PlayingResponseDto;
import com.sedlacek.quiz.dto.QuestionsDto;
import com.sedlacek.quiz.dto.StatesAndAnswersDto;
import com.sedlacek.quiz.entity.User;
import com.sedlacek.quiz.model.States;
import com.sedlacek.quiz.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CapitalService {
    Random random = new Random();
    private Map<String, String> chosenContinent;
    private List<String> failedStates;
    private List<String> succeededStates;
    private long score;
    private final UserRepository userRepository;


    public CapitalService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<String> generateRandomStates(Map<String, String> continent) {
        List<String> generatedStates = new ArrayList<>();
        List<String> statesFromChosenContinent = continent.keySet().stream().toList();
        while (generatedStates.size() < 10) {
            String generatedState = statesFromChosenContinent.get(random.nextInt(continent.size() - 1));
            if (!generatedStates.contains(generatedState)) {
                generatedStates.add(generatedState);
            }
        }
        return generatedStates;
    }

    public List<String> generateQuestions(String state, Map<String, String> continent) {
        List<String> generatedQuestions = new ArrayList<>();
        generatedQuestions.add(continent.get(state));
        List<String> capitals = continent.values().stream().toList();
        while (generatedQuestions.size() < 4) {
            String generatedQuestion = capitals.get(random.nextInt(continent.size() - 1));
            if (!generatedQuestions.contains(generatedQuestion)) {
                generatedQuestions.add(generatedQuestion);
            }
        }
        Collections.shuffle(generatedQuestions);
        return generatedQuestions;
    }

    public boolean rightAnswer(String state, String capital) {
        return capital.equals(state);
    }

    private List<String> getFailedStates() {
        if (failedStates == null) {
            failedStates = new ArrayList<>();
        }
        return failedStates;
    }

    private List<String> getSucceededStates() {
        if (succeededStates == null) {
            succeededStates = new ArrayList<>();
        }
        return succeededStates;
    }

    public void playTheQuiz(AnswersDto answers, List<String> states, User user) {
        score = 0;
        int index = 0;
        getFailedStates().clear();
        getSucceededStates().clear();
        for (String state : states) {
            if (answers.getAnswers().get(index) == null) {
                answers.getAnswers().set(index, "");
            }
            if (rightAnswer(chosenContinent.get(state), answers.getAnswers().get(index))) {
                user.addRightAnswer();
                succeededStates.add(state);
                score++;
            } else {
                user.addWrongAnswer();
                failedStates.add(state);
            }
            index++;
        }
    }

    public ResponseEntity<QuestionsDto> getQuestions(Map<String, String> continent) {
        List<String> generatedStates = generateRandomStates(continent);
        List<String> generatedCities = new ArrayList<>();
        for (String state: generatedStates) {
            List<String> cities = generateQuestions(state, continent);
            generatedCities.addAll(cities);
        }
        return ResponseEntity.ok(new QuestionsDto(generatedStates, generatedCities));
    }

    public ResponseEntity<PlayingResponseDto> submitAnswers(StatesAndAnswersDto statesAndAnswers) {
        switch (statesAndAnswers.getContinent()) {
            case "europe" -> chosenContinent = States.Europe;
            case "asia" -> chosenContinent = States.AsiaAndOceania;
            case "america" -> chosenContinent = States.NorthAndSouthAmerica;
            case "africa" -> chosenContinent = States.Africa;
            default -> chosenContinent = new HashMap<>();
        }
        User user = userRepository.findByUsername(statesAndAnswers.getUsername());
        playTheQuiz(statesAndAnswers.getAnswers(), statesAndAnswers.getStates(), user);
        user.addExp(score * 10);
        user.countPercentage();
        user.levelCheck();
        userRepository.save(user);
        return ResponseEntity.ok(new PlayingResponseDto(score, failedStates, succeededStates));
    }
}
