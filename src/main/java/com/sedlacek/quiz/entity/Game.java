package com.sedlacek.quiz.entity;

import com.sedlacek.quiz.model.GameType;
import jakarta.persistence.Entity;
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

    private GameType gameType;
    private User user;
    private int score;
    private int gameTime;
    private List<String> questions;
    private List<String> answers;

}
