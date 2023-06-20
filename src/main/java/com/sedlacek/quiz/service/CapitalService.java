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
    private List<String> correctStates;
    private long score;
    private final UserRepository userRepository;


    public CapitalService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<String> getTenRandomStates(Map<String, String> continent) {
        List<String> allStatesFromContinent = continent.keySet().stream().toList();
        List<String> states = new ArrayList<>();

        while (states.size() < 10) {
            String state = allStatesFromContinent.get(random.nextInt(continent.size() - 1));
            if (!states.contains(state)) {
                states.add(state);
            }
        }

        return states;
    }

    public List<String> getFourCapitals(String state, Map<String, String> continent) {
        List<String> allCapitalsFromContinent = continent.values().stream().toList();
        List<String> capitals = new ArrayList<>();

        capitals.add(continent.get(state));

        while (capitals.size() < 4) {
            String capital = allCapitalsFromContinent.get(random.nextInt(continent.size() - 1));
            if (!capitals.contains(capital)) {
                capitals.add(capital);
            }
        }

        Collections.shuffle(capitals);

        return capitals;
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

    private List<String> getCorrectStates() {
        if (correctStates == null) {
            correctStates = new ArrayList<>();
        }

        return correctStates;
    }

    public void playTheQuiz(AnswersDto answers, List<String> states, User user) {
        score = 0;
        getFailedStates().clear();
        getCorrectStates().clear();

        for (int i = 0; i < states.size(); i++) {
            String state = states.get(i);
            String answer = answers.getAnswers().get(i);

            if (answer == null) {
                answer = "";
                answers.getAnswers().set(i, answer);
            }

            if (rightAnswer(chosenContinent.get(state), answer)) {
                user.addRightAnswer();
                correctStates.add(state);
                score++;
            } else {
                user.addWrongAnswer();
                failedStates.add(state);
            }
        }
    }

    public ResponseEntity<QuestionsDto> getQuestions(Map<String, String> continent) {
        List<String> generatedStates = getTenRandomStates(continent);
        List<String> generatedCapitals = new ArrayList<>();

        for (String state: generatedStates) {
            List<String> capitals = getFourCapitals(state, continent);
            generatedCapitals.addAll(capitals);
        }

        return ResponseEntity.ok(new QuestionsDto(generatedStates, generatedCapitals));
    }

    public ResponseEntity<PlayingResponseDto> getResults(StatesAndAnswersDto statesAndAnswers) {
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

        return ResponseEntity.ok(new PlayingResponseDto(score, failedStates, correctStates));
    }
}
