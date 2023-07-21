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
public class QuestionsDto {
  
    private List<String> questions;

    private List<String> possibleAnswers;

    private GameType gameType;
}
