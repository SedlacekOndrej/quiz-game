package com.sedlacek.quiz.service;

import com.sedlacek.quiz.dto.*;
import com.sedlacek.quiz.entity.EntityBase;
import com.sedlacek.quiz.entity.Game;
import com.sedlacek.quiz.entity.User;
import com.sedlacek.quiz.model.Continent;
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

    private Game game;

    private final UserRepository userRepository;
    private final GameRepository gameRepository;


    public GameService(UserRepository userRepository, GameRepository gameRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }


    private Game getGame() {
        if (game == null) {
            return new Game();
        }
        return game;
    }

    public List<String> generateQuestions(Map<String, String> continent, int numberOfQuestions) {
        List<String> generatedQuestions = new ArrayList<>();
        List<String> statesFromChosenContinent = continent.keySet().stream().toList();

        while (generatedQuestions.size() < numberOfQuestions) {
            String generatedQuestion = statesFromChosenContinent.get(random.nextInt(continent.size() - 1));

            if (!generatedQuestions.contains(generatedQuestion)) {
                generatedQuestions.add(generatedQuestion);
            }
        }
        return generatedQuestions;
    }

    public List<String> generateFourPossibleAnswers(String question, Map<String, String> continent) {
        List<String> possibleAnswers = new ArrayList<>();
        List<String> allAnswers = continent.values().stream().toList();

        possibleAnswers.add(continent.get(question));
        getGame().addRightAnswer(continent.get(question));

        while (possibleAnswers.size() < 4) {
            String possibleAnswer = allAnswers.get(random.nextInt(continent.size() - 1));

            if (!possibleAnswers.contains(possibleAnswer)) {
                possibleAnswers.add(possibleAnswer);
            }
        }
        Collections.shuffle(possibleAnswers);

        return possibleAnswers;
    }

    public boolean rightAnswer(Map<String,String> continent, String question, String answer) {
        return answer.equals(continent.get(question));
    }

    public void playTheQuiz(List<String> answers, List<String> questions, User user, Map<String,String> continent) {
        int index = 0;

        game = getGame();

        game.setScore(0);
        game.setUser(user);

        for (String question : questions) {
            if (answers.get(index) == null) {
                answers.set(index, "");
            }
            if (rightAnswer(continent, question, answers.get(index))) {
                game.incrementScore();

                user.addRightAnswer();
            } else {
                user.addWrongAnswer();
            }
            index++;
        }
        user.addExp(game.getScore() * 10L);
    }

    public ResponseEntity<QuestionsDto> getQuestions(Continent continentName, GameType gameType, int numberOfQuestions) {
        game = new Game();

        Map<String,String> continent = continentSelection(continentName, gameType);

        game.setQuestions(generateQuestions(continent, numberOfQuestions));
        game.setPossibleAnswers(new ArrayList<>());

        for (String question: game.getQuestions()) {
            List<String> possibleAnswers = generateFourPossibleAnswers(question, continent);

            game.getPossibleAnswers().addAll(possibleAnswers);
        }
        return ResponseEntity.ok(new QuestionsDto(game.getQuestions(), game.getPossibleAnswers(), gameType));
    }

    public ResponseEntity<PlayingResponseDto> submitAnswers(QuestionsAndAnswersDto questionsAndAnswers) {
        GameType gameType = GameType.valueOf(questionsAndAnswers.getGameType());
        Continent continentName = Continent.valueOf(questionsAndAnswers.getContinent().toUpperCase());
        User user = userRepository.findByUsername(questionsAndAnswers.getUsername());

        Map<String,String> continent = continentSelection(continentName, gameType);
        playTheQuiz(questionsAndAnswers.getAnswers(), questionsAndAnswers.getQuestions(), user, continent);

        game.setGameType(gameType);
        game.setGameTime(questionsAndAnswers.getGameTime());
        game.setAnswers(questionsAndAnswers.getAnswers());

        user.countPercentage();
        user.levelCheck();
        user.addGame(game);

        userRepository.save(user);
        gameRepository.save(game);

        return ResponseEntity.ok(new PlayingResponseDto(game.getScore(), game.getRightAnswers()));
    }

    public ResponseEntity<List<GameHistoryDto>> getAllGamesHistory() {
        List<Game> games = gameRepository.findAllByOrderByScoreDescGameTimeAsc();
        List<GameHistoryDto> gameDtos = games.stream().map(entity -> {
            GameHistoryDto gameDto = EntityBase.convert(entity, GameHistoryDto.class);
            gameDto.setUserId(entity.getUser().getId());
            gameDto.setUsername(entity.getUser().getUsername());
            return gameDto;
        }).toList();

        return ResponseEntity.ok(gameDtos);
    }

    public ResponseEntity<List<EncyclopediaDto>> getEncyclopedia() {
        List<EncyclopediaDto> encyclopediaDtos = new ArrayList<>();

        for (String flag : Flags.getAllFlagsAndStates().keySet()) {
            String state = Flags.getAllFlagsAndStates().get(flag);
            String capital = Capitals.getAllStatesAndCapitals().get(state);

            encyclopediaDtos.add(new EncyclopediaDto(flag, state, capital));
        }
        encyclopediaDtos.sort(Comparator.comparing(EncyclopediaDto::getStateName));

        return ResponseEntity.ok(encyclopediaDtos);
    }

    private Map<String,String> continentSelection(Continent continent, GameType gameType) {
        getGame().setContinentName(continent);

        if (gameType == GameType.CAPITALS) {

            switch (continent) {
                case EUROPE -> {
                    return Capitals.Europe;
                }
                case ASIA -> {
                    return Capitals.AsiaAndOceania;
                }
                case AMERICA -> {
                    return Capitals.NorthAndSouthAmerica;
                }
                case AFRICA -> {
                    return Capitals.Africa;
                }
                default -> {
                    return new HashMap<>();
                }
            }
        }
        if (gameType == GameType.FLAGS) {

            switch (continent) {
                case EUROPE -> {
                    return Flags.Europe;
                }
                case ASIA -> {
                    return Flags.AsiaAndOceania;
                }
                case AMERICA -> {
                    return Flags.NorthAndSouthAmerica;
                }
                case AFRICA -> {
                    return Flags.Africa;
                }
                default -> {
                    return new HashMap<>();
                }
            }
        }
        return new HashMap<>();
    }
}
