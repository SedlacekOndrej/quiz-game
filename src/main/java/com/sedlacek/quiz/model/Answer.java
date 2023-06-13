package com.sedlacek.quiz.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Answer {
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

    public Answer(String answer1, String answer2, String answer3, String answer4, String answer5,
                  String answer6, String answer7, String answer8, String answer9, String answer10) {
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.answer5 = answer5;
        this.answer6 = answer6;
        this.answer7 = answer7;
        this.answer8 = answer8;
        this.answer9 = answer9;
        this.answer10 = answer10;
    }

    public List<String> answeredCapitals() {
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
