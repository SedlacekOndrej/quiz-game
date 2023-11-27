package com.sedlacek.quiz.service;

import com.sedlacek.quiz.dto.*;
import com.sedlacek.quiz.entity.EntityBase;
import com.sedlacek.quiz.entity.Game;
import com.sedlacek.quiz.entity.User;
import com.sedlacek.quiz.model.Capitals;
import com.sedlacek.quiz.model.Continent;
import com.sedlacek.quiz.model.Flags;
import com.sedlacek.quiz.model.GameType;
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

    /**
     * This method checks if there is already a game object created and returns it. If there isn't, the method creates
     * a new one.
     *
     * @return game if already exists, a new Game() otherwise
     */
    private Game getGame() {
        if (game == null) {
            return new Game();
        }
        return game;
    }

    /**
     * This method generates random states from chosen continent. Number of states depends on the selected number
     * of questions.
     *
     * @param continent         continent from which the states are generated
     * @param numberOfQuestions number of questions which user wants to answer
     * @return list of states as questions
     */
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

    /**
     * This method generates one right answer and three wrong answers to a state which is given as a parameter.
     * The answers are chosen from the selected continent and are returned in a shuffled list.
     *
     * @param question  state which the answers need to be generated to
     * @param continent continent from which the answers are generated
     * @return one right answer and three wrong answers to a question in a list
     */
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

    /**
     * This method takes a user's answer and compares it with right answer to a given question. If the user's answer
     * is right, the method returns true, if not, then it returns false.
     *
     * @param continent selected continent from which are the questions and answers generated
     * @param question  state as a question
     * @param answer    user's answer
     * @return true if the answer is right, otherwise false
     */
    public boolean rightAnswer(Map<String, String> continent, String question, String answer) {
        return answer.equals(continent.get(question));
    }

    /**
     * This method compares questions and user's answers from selected continent. If the answer is right, the user
     * gains experience points which is added to its account at the end of the game.
     *
     * @param answers   list of states as questions
     * @param questions list of capitals or state names as answers
     * @param user      logged user that plays the game
     * @param continent selected continent from which are the questions and answers generated
     */
    public void playTheQuiz(List<String> answers, List<String> questions, User user, Map<String, String> continent) {
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

    /**
     * This method sends to front-end what type of game is going to be played, selected number of questions, which are
     * randomly generated states and four possible answers to each question, where always only one of them is right.
     *
     * @param continentName     name of the continent from which the states are generated
     * @param gameType          specifies what type of game is going to be played (o. g. capitals or flags)
     * @param numberOfQuestions specifies the number of generated questions
     * @return generated questions, four possible answers to each of them and type of the game
     */
    public ResponseEntity<QuestionsDto> getQuestions(Continent continentName, GameType gameType, int numberOfQuestions) {
        game = getGame();

        Map<String, String> continent = continentSelection(continentName, gameType);

        game.setQuestions(generateQuestions(continent, numberOfQuestions));
        game.setPossibleAnswers(new ArrayList<>());

        for (String question : game.getQuestions()) {
            List<String> possibleAnswers = generateFourPossibleAnswers(question, continent);

            game.getPossibleAnswers().addAll(possibleAnswers);
        }
        return ResponseEntity.ok(new QuestionsDto(game.getQuestions(), game.getPossibleAnswers(), gameType));
    }

    /**
     * This method invokes playTheQuiz method and takes care of consequences. It counts success percentage of the user,
     * check and count its level and add the game to the user's game history. Then the user and the game are saved
     * to the database.
     *
     * @param questionsAndAnswers contains information from front-end about the game - selected continent, game time,
     *                            game type, questions, answers and name of the user who has played the game
     * @return score which user has achieved and right answers to the questions
     */
    public ResponseEntity<PlayingResponseDto> submitAnswers(QuestionsAndAnswersDto questionsAndAnswers) {
        GameType gameType = GameType.valueOf(questionsAndAnswers.getGameType());
        Continent continentName = Continent.valueOf(questionsAndAnswers.getContinent().toUpperCase());
        User user = userRepository.findByUsername(questionsAndAnswers.getUsername());

        Map<String, String> continent = continentSelection(continentName, gameType);
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

    /**
     * This method selects all the games played from the database, converts it to DTO a returns it in Json format.
     *
     * @return list of all games played by all users
     */
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

    /**
     * This method gathers states from all continents, assign them corresponding flag and capital and returns it in Json
     * format.
     *
     * @return list of all state names, its flags and capitals
     */
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

    /**
     * This method returns a hash map with a states or flags as the keys and capitals or state names as the values.
     *
     * @param continent continent name as enum
     * @param gameType  type of the game as enum
     * @return selected continent hash map
     */
    private Map<String, String> continentSelection(Continent continent, GameType gameType) {
        getGame().setContinentName(continent);

        Map<String, String> result = new HashMap<>();

        switch (gameType) {
            case CAPITALS -> {
                switch (continent) {
                    case EUROPE -> result = Capitals.Europe;
                    case ASIA -> result = Capitals.AsiaAndOceania;
                    case AMERICA -> result = Capitals.NorthAndSouthAmerica;
                    case AFRICA -> result = Capitals.Africa;
                }
            }
            case FLAGS -> {
                switch (continent) {
                    case EUROPE -> result = Flags.Europe;
                    case ASIA -> result = Flags.AsiaAndOceania;
                    case AMERICA -> result = Flags.NorthAndSouthAmerica;
                    case AFRICA -> result = Flags.Africa;
                }
            }
        }
        return result;
        }
}
