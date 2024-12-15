package com.sedlacek.quiz.validator;

import com.sedlacek.quiz.exception.ValidationException;

public abstract class Validator<T> {

    public abstract void validate(T object) throws ValidationException;
}
