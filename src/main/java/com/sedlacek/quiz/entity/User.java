package com.sedlacek.quiz.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
public class User extends EntityBase {
    private String username;
    private String password;
    private String email;
    private int level;
    private long exp;
    private int rightAnswers;
    private int wrongAnswers;
    private double percentage;

    @ElementCollection
    private static final List<Integer> levelLimits = List.of(0, 100, 250, 450, 700, 1000, 1500, 2300, 3300, 4800,
            6800, 9300, 12300, 15800, 18800, 23000, 28000, 34000, 41500, 50000);

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.level = 1;
        this.exp = 0L;
        this.rightAnswers = 0;
        this.wrongAnswers = 0;
        this.percentage = 0.00;
    }

    public void addExp(long exp) {
        this.exp = this.exp + exp;
    }

    public void addRightAnswer() {
        rightAnswers++;
    }

    public void addWrongAnswer() {
        wrongAnswers++;
    }

    public void countPercentage() {
        setPercentage((double)rightAnswers / (rightAnswers + wrongAnswers) * 100);
    }

    public void levelCheck() {
        for (int i = levelLimits.size() - 1; i > 0; i--) {
            if (exp / levelLimits.get(i) >= 1) {
                setLevel(levelLimits.indexOf(levelLimits.get(i)) + 1);
                return;
            }
        }
    }
}
