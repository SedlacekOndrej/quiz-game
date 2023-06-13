package com.sedlacek.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswersDto {
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String answer5;
    private String answer6;
    private String answer7;
    private String answer8;
    private String answer9;
    private String answer10;

    public List<String> getAnswers() {
        List<String> capitals = new ArrayList<>();
        capitals.add(answer1);
        capitals.add(answer2);
        capitals.add(answer3);
        capitals.add(answer4);
        capitals.add(answer5);
        capitals.add(answer6);
        capitals.add(answer7);
        capitals.add(answer8);
        capitals.add(answer9);
        capitals.add(answer10);
        return capitals;
    }
}
