package com.sedlacek.quiz.dto;

import com.sedlacek.quiz.entity.Country;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
    private Country country;
    private String rightAnswer;
    private Set<String> allAnswers;
}
