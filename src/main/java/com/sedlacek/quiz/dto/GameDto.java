package com.sedlacek.quiz.dto;

import com.sedlacek.quiz.model.GameType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameDto implements Serializable {

    private OffsetDateTime createdDate;

    private String continentName;

    private GameType gameType;

    private int score;

    private int gameTime;

    private List<String> questions;

    private List<String> possibleAnswers;

    private List<String> answers;

    private List<String> rightAnswers;
}
