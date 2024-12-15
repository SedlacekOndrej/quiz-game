package com.sedlacek.quiz.exception;

import java.util.Date;

@SuppressWarnings("unused")
public record ErrorDetails(Date timestamp, String message, String details, Integer status, String error) {
}