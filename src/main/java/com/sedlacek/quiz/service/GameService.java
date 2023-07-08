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

@Service
public class GameService {
    Random random = new Random();
    private Map<String, String> chosenContinent;
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

    public void playTheQuiz(AnswersDto answers, List<String> questions, User user, GameType gameType) {
        game.setScore(0);
        int index = 0;
        game.setUser(user);
        game.getFailedQuestions().clear();
        game.getSucceededQuestions().clear();
        for (String question : questions) {
            if (answers.getAnswers().get(index) == null) {
                answers.getAnswers().set(index, "");
            }
            if (rightAnswer(chosenContinent.get(question), answers.getAnswers().get(index))) {
                user.addRightAnswer();
                switch (gameType) {
                    case CAPITALS -> game.addSucceededQuestion(question);
                    case FLAGS -> game.addSucceededQuestion(answers.getAnswers().get(index));
                }
                game.incrementScore();
            } else {
                user.addWrongAnswer();
                switch (gameType) {
                    case CAPITALS -> game.addFailedQuestion(question);
                    case FLAGS -> game.addFailedQuestion(answers.getAnswers().get(index));
                }
            }
            index++;
        }
        user.addExp(game.getScore() * 10L);
    }

    public ResponseEntity<QuestionsDto> getQuestions(String continent, GameType gameType) {
        continentSelection(continent, gameType);
        game = new Game();
        game.setQuestions(generateTenQuestions(chosenContinent));
        game.setPossibleAnswers(new ArrayList<>());

        for (String question: game.getQuestions()) {
            List<String> possibleAnswers = generateFourPossibleAnswers(question, chosenContinent);
            game.getPossibleAnswers().addAll(possibleAnswers);
        }
        return ResponseEntity.ok(new QuestionsDto(game.getQuestions(), game.getPossibleAnswers(), gameType));
    }

    public ResponseEntity<PlayingResponseDto> submitAnswers(QuestionsAndAnswersDto questionsAndAnswers) {
        GameType gameType = GameType.valueOf(questionsAndAnswers.getGameType());
        continentSelection(questionsAndAnswers.getContinent(), gameType);
        User user = userRepository.findByUsername(questionsAndAnswers.getUsername());
        playTheQuiz(questionsAndAnswers.getAnswers(), questionsAndAnswers.getStates(), user, gameType);

        game.setGameType(gameType);
        game.setGameTime(questionsAndAnswers.getGameTime());
        game.setAnswers(questionsAndAnswers.getAnswers().getAnswers());

        user.countPercentage();
        user.levelCheck();
        user.addGame(game);

        userRepository.save(user);
        gameRepository.save(game);
        return ResponseEntity.ok(new PlayingResponseDto(game.getScore(), game.getFailedQuestions(), game.getSucceededQuestions()));
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
