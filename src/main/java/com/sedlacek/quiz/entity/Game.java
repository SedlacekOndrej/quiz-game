package com.sedlacek.quiz.entity;

import com.sedlacek.quiz.model.Continent;
import com.sedlacek.quiz.model.GameType;
import jakarta.persistence.*;
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

    private Continent continentName;

    private GameType gameType;

    private int score;

    private int gameTime;

    @Column(length = 1000)
    private List<String> questions;

    @Column(length = 4000)
    private List<String> possibleAnswers;

    @Column(length = 1000)
    private List<String> answers;

    @Column(length = 1000)
    private List<String> rightAnswers = new ArrayList<>();

    @ManyToOne
    private User user;


    public void addRightAnswer(String answer) { this.rightAnswers.add(answer); }

    public void incrementScore() {
        this.score++;
    }
}
