package com.sedlacek.quiz.dto;

import com.sedlacek.quiz.model.GameType;
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
public class QuestionsDto {
  
    private Set<String> questions;

    private List<String> possibleAnswers;

    private GameType gameType;
}
