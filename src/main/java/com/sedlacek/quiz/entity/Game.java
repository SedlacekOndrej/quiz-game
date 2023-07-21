package com.sedlacek.quiz.entity;

import com.sedlacek.quiz.model.GameType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@Table(name = "games")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Game extends EntityBase {

    @Transient
    private Map<String, String> continent;

    private String continentName;

    private GameType gameType;

    private int score;

    private int gameTime;

    private Set<String> questions;

    private List<String> possibleAnswers;

    private List<String> answers;

    private Set<String> rightAnswers = new HashSet<>();

    @ManyToOne
    private User user;


    public void addRightAnswer(String answer) { this.rightAnswers.add(answer); }

    public void incrementScore() {
        this.score++;
    }
}
