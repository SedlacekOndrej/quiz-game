package com.sedlacek.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatesDto {
    private String state1;
    private String state2;
    private String state3;
    private String state4;
    private String state5;
    private String state6;
    private String state7;
    private String state8;
    private String state9;
    private String state10;

    public List<String> getStates() {
        List<String> states = new ArrayList<>();
        states.add(state1);
        states.add(state2);
        states.add(state3);
        states.add(state4);
        states.add(state5);
        states.add(state6);
        states.add(state7);
        states.add(state8);
        states.add(state9);
        states.add(state10);
        return states;
    }
}
