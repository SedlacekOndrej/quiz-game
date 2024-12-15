package com.sedlacek.quiz.entity;

import com.sedlacek.quiz.model.Continent;
import com.sedlacek.quiz.model.GameType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "games")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Game extends EntityBase {
    private Continent continentName;
    private GameType gameType;
    private int score;
    private int gameTime;
    @Column(length = 2000)
    private List<Question> questions;
    @Column(length = 1000)
    private List<String> answers;
    @ManyToOne
    private User user;
    private String userName;

    public void incrementScore() {
        this.score++;
    }
}
