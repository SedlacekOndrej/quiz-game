package com.sedlacek.quiz.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
@SuppressWarnings("unused")
public class ErrorDetails {

    private final Date timestamp;

    private final String message;

    private final String details;

    private final Integer status;

    private final String error;
}