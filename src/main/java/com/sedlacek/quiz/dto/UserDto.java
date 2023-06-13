package com.sedlacek.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private long id;
    private String username;
    private String password;
    private String email;
    private int level = 1;
    private long exp = 0L;
    private int rightAnswers = 0;
    private int wrongAnswers = 0;
    private double percentage = 0.00;
}
