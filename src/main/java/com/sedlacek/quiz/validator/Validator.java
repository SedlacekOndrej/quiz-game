package com.sedlacek.quiz.validator;

public abstract class Validator<T> {

    public abstract void validate(T object);
}
