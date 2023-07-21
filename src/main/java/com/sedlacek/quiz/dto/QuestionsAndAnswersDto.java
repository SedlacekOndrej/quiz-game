package com.sedlacek.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionsAndAnswersDto {

    private String username;

    private String continent;

    private Set<String> questions;

    private List<String> answers;

    private String gameType;

    private int gameTime;
}
