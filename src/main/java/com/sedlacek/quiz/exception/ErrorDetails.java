package com.sedlacek.quiz.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
@SuppressWarnings("unused")
public record ErrorDetails(Date timestamp, String message, String details, Integer status, String error) {
}