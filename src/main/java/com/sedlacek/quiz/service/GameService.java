package com.sedlacek.quiz.service;

import com.sedlacek.quiz.dto.*;
import com.sedlacek.quiz.entity.EntityBase;
import com.sedlacek.quiz.entity.Game;
import com.sedlacek.quiz.entity.User;
import com.sedlacek.quiz.model.Flags;
import com.sedlacek.quiz.model.GameType;
import com.sedlacek.quiz.model.Capitals;
import com.sedlacek.quiz.repository.GameRepository;
import com.sedlacek.quiz.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameService {
    Random random = new Random();
    private Map<String, String> chosenContinent;
    private List<String> failedQuestions;
    private List<String> succeededQuestions;
    private int score;
    private Game game;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;


    public GameService(UserRepository userRepository, GameRepository gameRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    public List<String> generateTenQuestions(Map<String, String> continent) {
        List<String> generatedQuestions = new ArrayList<>();
        List<String> statesFromChosenContinent = continent.keySet().stream().toList();
        while (generatedQuestions.size() < 10) {
            String generatedState = statesFromChosenContinent.get(random.nextInt(continent.size() - 1));
            if (!generatedQuestions.contains(generatedState)) {
                generatedQuestions.add(generatedState);
            }
        }
        return generatedQuestions;
    }

    public List<String> generateFourPossibleAnswers(String question, Map<String, String> continent) {
        List<String> possibleAnswers = new ArrayList<>();
        possibleAnswers.add(continent.get(question));
        List<String> allAnswers = continent.values().stream().toList();
        while (possibleAnswers.size() < 4) {
            String possibleAnswer = allAnswers.get(random.nextInt(continent.size() - 1));
            if (!possibleAnswers.contains(possibleAnswer)) {
                possibleAnswers.add(possibleAnswer);
            }
        }
        Collections.shuffle(possibleAnswers);
        return possibleAnswers;
    }

    public boolean rightAnswer(String question, String answer) {
        return answer.equals(question);
    }

    private List<String> getFailedQuestions() {
        if (failedQuestions == null) {
            failedQuestions = new ArrayList<>();
        }
        return failedQuestions;
    }

    private List<String> getSucceededQuestions() {
        if (succeededQuestions == null) {
            succeededQuestions = new ArrayList<>();
        }
        return succeededQuestions;
    }

    public void playTheQuiz(AnswersDto answers, List<String> questions, User user, GameType gameType) {
        score = 0;
        int index = 0;
        game = new Game();
        game.setUser(user);
        getFailedQuestions().clear();
        getSucceededQuestions().clear();
        for (String question : questions) {
            if (answers.getAnswers().get(index) == null) {
                answers.getAnswers().set(index, "");
            }
            if (rightAnswer(chosenContinent.get(question), answers.getAnswers().get(index))) {
                user.addRightAnswer();
                switch (gameType) {
                    case CAPITALS -> succeededQuestions.add(question);
                    case FLAGS -> succeededQuestions.add(answers.getAnswers().get(index));
                }
                score++;
            } else {
                user.addWrongAnswer();
                switch (gameType) {
                    case CAPITALS -> failedQuestions.add(question);
                    case FLAGS -> failedQuestions.add(answers.getAnswers().get(index));
                }
            }
            index++;
        }
        game.setScore(score);
    }

    public ResponseEntity<QuestionsDto> getQuestions(String continent, GameType gameType) {
        continentSelection(continent, gameType);
        game.setQuestions(generateTenQuestions(chosenContinent));

        for (String question: game.getQuestions()) {
            List<String> possibleAnswers = generateFourPossibleAnswers(question, chosenContinent);
            game.getPossibleAnswers().addAll(possibleAnswers);
        }
        return ResponseEntity.ok(new QuestionsDto(game.getQuestions(), game.getPossibleAnswers(), gameType));
    }

    public ResponseEntity<PlayingResponseDto> submitAnswers(QuestionsAndAnswersDto questionsAndAnswers) {
        continentSelection(questionsAndAnswers.getContinent(), questionsAndAnswers.getGameType());
        User user = userRepository.findByUsername(questionsAndAnswers.getUsername());
        playTheQuiz(questionsAndAnswers.getAnswers(), questionsAndAnswers.getStates(), user, questionsAndAnswers.getGameType());
        game.setGameType(questionsAndAnswers.getGameType());
        game.setGameTime(questionsAndAnswers.getGameTime());
        game.setAnswers(questionsAndAnswers.getAnswers().getAnswers());
        user.addExp(score * 10L);
        user.countPercentage();
        user.levelCheck();
        user.addGame(game);
        userRepository.save(user);
        gameRepository.save(game);
        return ResponseEntity.ok(new PlayingResponseDto(score, failedQuestions, succeededQuestions));
    }

    public ResponseEntity<List<GameDto>> getAllGamesHistory() {
        List<Game> games = gameRepository.findAllByOrderByCreatedDate();
        List<GameDto> gameDtos = games.stream().map(gameEntity -> EntityBase.convert(gameEntity, GameDto.class)).toList();
        return ResponseEntity.ok(gameDtos);
    }

    public ResponseEntity<List<GameDto>> getUserGamesHistory(long userId) {
        List<Game> games = gameRepository.findAllByUserIdOrderByCreatedDate(userId);
        List<GameDto> gameDtos = games.stream().map(gameEntity -> EntityBase.convert(gameEntity, GameDto.class)).toList();
        return ResponseEntity.ok(gameDtos);
    }

    private void continentSelection(String continent, GameType gameType) {
        if (gameType == GameType.CAPITALS) {
            switch (continent) {
                case "europe" -> chosenContinent = Capitals.Europe;
                case "asia" -> chosenContinent = Capitals.AsiaAndOceania;
                case "america" -> chosenContinent = Capitals.NorthAndSouthAmerica;
                case "africa" -> chosenContinent = Capitals.Africa;
                default -> chosenContinent = new HashMap<>();
            }
        }
        if (gameType == GameType.FLAGS) {
            switch (continent) {
                case "europe" -> chosenContinent = Flags.Europe;
                case "asia" -> chosenContinent = Flags.AsiaAndOceania;
                case "america" -> chosenContinent = Flags.NorthAndSouthAmerica;
                case "africa" -> chosenContinent = Flags.Africa;
                default -> chosenContinent = new HashMap<>();
            }
        }
    }
}
