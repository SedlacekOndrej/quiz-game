package com.sedlacek.quiz.service;

import com.sedlacek.quiz.dto.PlayingResponseDto;
import com.sedlacek.quiz.dto.QuestionsAndAnswersDto;
import com.sedlacek.quiz.dto.QuestionsDto;
import com.sedlacek.quiz.entity.User;
import com.sedlacek.quiz.model.Capitals;
import com.sedlacek.quiz.model.GameType;
import com.sedlacek.quiz.repository.GameRepository;
import com.sedlacek.quiz.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameServiceTest {

    private UserRepository fakeUserRepository;

    private GameService gameService;

    @BeforeEach
    public void init() {
        fakeUserRepository = mock(UserRepository.class);

        GameRepository fakeGameRepository = mock(GameRepository.class);

        gameService = new GameService(fakeUserRepository, fakeGameRepository);
    }

    @Test
    void generateQuestions_CountIsOk() {
        List<String> tenQuestions = gameService.generateQuestions(Capitals.Europe, 10);
        List<String> twentyQuestions = gameService.generateQuestions(Capitals.Europe, 20);

        assertEquals(10, tenQuestions.size());
        assertEquals(20, twentyQuestions.size());
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
        QuestionsDto questions = gameService.getQuestions("europe", GameType.CAPITALS, 10).getBody();

        assert questions != null;

        assertEquals(10, questions.getQuestions().size());

        assertEquals(40, questions.getPossibleAnswers().size());

        assertTrue(questions.getQuestions().get(0).length() > 2);
    }

    @Test
    void getQuestions_CountOfQuestionsIsOk_And_ContainsFlags_True() {
        QuestionsDto questions = gameService.getQuestions("europe", GameType.FLAGS, 10).getBody();

        assert questions != null;

        assertEquals(10, questions.getQuestions().size());

        assertEquals(40, questions.getPossibleAnswers().size());

        assertEquals(2, questions.getQuestions().get(0).length());
    }

    @Test
    void playTheQuiz_GivenNineRightAnswers_ScoreIsNine() {
        User user = new User("TestUser", "password123", "TestUser@gmail.com",
                1, 0L, 0, 0, 0.00, new ArrayList<>());

        List<String> questions = List.of("Slovensko", "Německo", "Polsko", "Itálie", "Francie", "Španělsko",
                "Portugalsko", "Irsko", "Chorvatsko", "Švýcarsko");

        List<String> answers = List.of("Bratislava", "Berlín", "Varšava", "Řím", "Paříž", "Madrid",
                "Lisabon", "Dublin", "Záhřeb", "Praha");

        gameService.playTheQuiz(answers, questions, user, Capitals.Europe);

        assertEquals(90, user.getExp());
    }

    @Test
    void submitAnswers_GivenSixRightAnswers_ScoreIsSix() {
        User mockUser = mock(User.class);

        when(fakeUserRepository.findByUsername(anyString())).thenReturn(mockUser);

        List<String> questions = List.of("Slovensko", "Německo", "Polsko", "Itálie", "Francie", "Španělsko",
                "Portugalsko", "Irsko", "Chorvatsko", "Švýcarsko");

        List<String> answers = List.of("Bratislava", "Berlín", "Varšava", "Řím", "Paříž", "Madrid",
                "Brusel", "Oslo", "Helsinki", "Praha");

        QuestionsAndAnswersDto questionsAndAnswers = new QuestionsAndAnswersDto("TestUser", "europe",
                questions, answers, "CAPITALS", 10);

        PlayingResponseDto playingResponse = gameService.submitAnswers(questionsAndAnswers).getBody();

        assert playingResponse != null;

        assertEquals(6, playingResponse.getScore());
    }

}