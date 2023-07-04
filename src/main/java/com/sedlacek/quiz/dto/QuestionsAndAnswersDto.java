package com.sedlacek.quiz.dto;

import com.sedlacek.quiz.model.GameType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionsAndAnswersDto {
    private String username;
    private String continent;
    private List<String> states;
    private AnswersDto answers;
    private GameType gameType;
    private int gameTime;
}
