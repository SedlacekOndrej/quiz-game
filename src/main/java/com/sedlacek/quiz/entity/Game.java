package com.sedlacek.quiz.entity;

import com.sedlacek.quiz.model.GameType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "games")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Game extends EntityBase {

    private GameType gameType;
    private int score;
    private int gameTime;
    private List<String> questions;
    private List<String> answers;
    @ManyToOne
    private User user;

}
