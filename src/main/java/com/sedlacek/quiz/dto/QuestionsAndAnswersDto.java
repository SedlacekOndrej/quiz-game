package com.sedlacek.quiz.dto;

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
    private List<String> questions;
    private AnswersDto answers;
    private String gameType;
    private int gameTime;
}
