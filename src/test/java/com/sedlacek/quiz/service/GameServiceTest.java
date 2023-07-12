package com.sedlacek.quiz.service;

import com.sedlacek.quiz.dto.AnswersDto;
import com.sedlacek.quiz.dto.PlayingResponseDto;
import com.sedlacek.quiz.dto.QuestionsAndAnswersDto;
import com.sedlacek.quiz.dto.QuestionsDto;
import com.sedlacek.quiz.entity.Game;
import com.sedlacek.quiz.entity.User;
import com.sedlacek.quiz.model.Capitals;
import com.sedlacek.quiz.model.GameType;
import com.sedlacek.quiz.repository.GameRepository;
import com.sedlacek.quiz.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    GameService gameService;

    @BeforeEach
    public void init() {
        UserRepository fakeUserRepository = Mockito.mock(UserRepository.class);

        GameRepository fakeGameRepository = Mockito.mock(GameRepository.class);

        gameService = new GameService(fakeUserRepository, fakeGameRepository);
    }

    @Test
    void generateTenQuestions_CountIsOk() {
        List<String> questions = gameService.generateTenQuestions(Capitals.Europe);

        assertEquals(10, questions.size());
    }

    @Test
    void generateFourPossibleAnswers_CountIsOk_And_ContainsRightAnswer() {
        List<String> possibleAnswers = gameService.generateFourPossibleAnswers("Slovensko", Capitals.Europe);

        assertEquals(4, possibleAnswers.size());

        assertTrue(possibleAnswers.contains("Bratislava"));
    }

    @Test
    void rightAnswer_GivenRightAnswer_ReturnsTrue() {
        assertTrue(gameService.rightAnswer(Capitals.Europe, "Slovensko", "Bratislava"));
    }

    @Test
    void rightAnswer_GivenWrongAnswer_ReturnsFalse() {
        assertFalse(gameService.rightAnswer(Capitals.Europe, "Slovensko", "Praha"));
    }

    @Test
    void getQuestions_CountOfQuestionsIsOk_And_ContainsCapitals_True() {
        QuestionsDto questions = gameService.getQuestions("europe", GameType.CAPITALS).getBody();

        assert questions != null;

        assertEquals(10, questions.getQuestions().size());

        assertEquals(40, questions.getPossibleAnswers().size());

        assertTrue(questions.getQuestions().get(0).length() > 2);
    }

    @Test
    void getQuestions_CountOfQuestionsIsOk_And_ContainsFlags_True() {
        QuestionsDto questions = gameService.getQuestions("europe", GameType.FLAGS).getBody();

        assert questions != null;

        assertEquals(10, questions.getQuestions().size());

        assertEquals(40, questions.getPossibleAnswers().size());

        assertEquals(2, questions.getQuestions().get(0).length());
    }

    @Test
    void playTheQuiz_GivenNineRightAnswers_ScoreIsNine() {
        Game game = new Game();

        User user = new User("TestUser", "password123", "TestUser@gmail.com",
                1, 0L, 0, 0, 0.00, new ArrayList<>());

        List<String> questions = List.of("Slovensko", "Německo", "Polsko", "Itálie", "Francie", "Španělsko",
                "Portugalsko", "Irsko", "Chorvatsko", "Švýcarsko");

        AnswersDto answers = new AnswersDto("Bratislava", "Berlín", "Varšava", "Řím", "Paříž", "Madrid",
                "Lisabon", "Dublin", "Záhřeb", "Praha");

        gameService.playTheQuiz(answers, questions, user);

        assertEquals(9, game.getScore());

        assertEquals(90, user.getExp());
    }

    @Test
    void submitAnswers_GivenSixRightAnswers_ScoreIsSix() {
        List<String> questions = List.of("Slovensko", "Německo", "Polsko", "Itálie", "Francie", "Španělsko",
                "Portugalsko", "Irsko", "Chorvatsko", "Švýcarsko");

        AnswersDto answers = new AnswersDto("Bratislava", "Berlín", "Varšava", "Řím", "Paříž", "Madrid",
                "Brusel", "Oslo", "Helsinki", "Praha");

        QuestionsAndAnswersDto questionsAndAnswers = new QuestionsAndAnswersDto("TestUser", "europe",
                questions, answers, "CAPITALS", 10);

        PlayingResponseDto playingResponse = gameService.submitAnswers(questionsAndAnswers).getBody();

        assert playingResponse != null;

        assertEquals(6, playingResponse.getScore());
    }

}