package com.sedlacek.quiz.service.impl;

import com.sedlacek.quiz.dto.GameDto;
import com.sedlacek.quiz.entity.Country;
import com.sedlacek.quiz.entity.Game;
import com.sedlacek.quiz.entity.Question;
import com.sedlacek.quiz.entity.User;
import com.sedlacek.quiz.exception.ResourceNotFoundException;
import com.sedlacek.quiz.model.Continent;
import com.sedlacek.quiz.model.GameType;
import com.sedlacek.quiz.repository.CountryRepository;
import com.sedlacek.quiz.repository.GameRepository;
import com.sedlacek.quiz.repository.UserRepository;
import com.sedlacek.quiz.service.GameService;
import com.sedlacek.quiz.utils.Constants;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GameServiceImpl implements GameService {
    private final Random random = new Random();
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final CountryRepository countryRepository;


    public GameServiceImpl(UserRepository userRepository, GameRepository gameRepository,
                           CountryRepository countryRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.countryRepository = countryRepository;
    }


    @Override
    public Game createGame(Continent continent, long userId, GameType gameType, int numberOfQuestions)
            throws ResourceNotFoundException {
        Game game = new Game();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_WITH_ID + userId + Constants.NOT_FOUND));
        game.setUser(user);
        game.setUserName(user.getUsername());
        game.setContinentName(continent);
        game.setGameType(gameType);
        game.setQuestions(getQuestions(continent, gameType, numberOfQuestions));
        gameRepository.save(game);
        user.setLastGameId(game.getId());
        userRepository.save(user);
        return game;
    }

    @Override
    public Game evaluateGame(GameDto gameDto) throws ResourceNotFoundException {
        Game game = gameRepository.findById(gameDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException(Constants.GAME_NOT_FOUND));

        for (Question question : game.getQuestions()) {
            if (Objects.equals(getRightAnswerForCountry(question.getCountry(), game.getGameType()),
                    gameDto.getAnswers().get(game.getQuestions().indexOf(question)))) {
                game.incrementScore();
            }
        }
        game.setGameTime(gameDto.getGameTime());
        game.setAnswers(gameDto.getAnswers());
        gameRepository.save(game);
        return game;
    }

    @Override
    public List<Game> getAllGamesHistory() {
        return gameRepository.findAllByOrderByScoreDescGameTimeAsc();
    }

    private List<Question> getQuestions(Continent continent, GameType gameType, int numberOfQuestions) {
        return generateCountriesAsQuestions(continent, numberOfQuestions, gameType);
    }

    private List<Question> generateCountriesAsQuestions(Continent continent, int numberOfQuestions, GameType gameType) {
        List<Country> allCountriesFromContinent = countryRepository.getCountriesByContinent(continent);
        Set<Country> countries = generateSpecificNumberOfRandomCountries(allCountriesFromContinent, numberOfQuestions);
        List<Question> questions = new ArrayList<>();

        for (Country country : countries) {
            Question question = generateQuestion(country, allCountriesFromContinent, gameType);
            questions.add(question);
        }
        return questions;
    }

    private Set<Country> generateSpecificNumberOfRandomCountries(List<Country> allCountriesFromContinent,
                                                                 int numberOfCountriesToGenerate) {
        Set<Country> countries = new HashSet<>();

        while (countries.size() < numberOfCountriesToGenerate) {
            Country randomCountry = allCountriesFromContinent.get(
                    random.nextInt(allCountriesFromContinent.size() - 1));
            countries.add(randomCountry);
        }
        return countries;
    }

    private Question generateQuestion(Country country, List<Country> allCountriesFromContinent, GameType gameType) {
        Question question = new Question();
        Set<String> allAnswers = new HashSet<>();

        String rightAnswer = getRightAnswerForCountry(country, gameType);
        allAnswers.add(rightAnswer);

        while (allAnswers.size() < 4) {
            Country randomCountry = allCountriesFromContinent.get(
                    random.nextInt(allCountriesFromContinent.size() - 1));

            String wrongAnswer = getRightAnswerForCountry(randomCountry, gameType);
            allAnswers.add(wrongAnswer);
        }
        question.setCountry(country);
        question.setRightAnswer(rightAnswer);
        question.setAllAnswers(allAnswers);
        return question;
    }

    private String getRightAnswerForCountry(Country country, GameType gameType) {
        if (gameType == GameType.CAPITALS) {
            return countryRepository.getCountryByCountryName(country.getCountryName()).getCapitalName();
        }
        if (gameType == GameType.FLAGS) {
            return countryRepository.getCountryByCountryName(country.getCountryName()).getCountryName();
        }
        return null;
    }
}
