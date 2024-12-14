package com.sedlacek.quiz.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Question implements Serializable {
    private Country country;
    private String rightAnswer;
    private Set<String> allAnswers;
}
