package com.sedlacek.quiz.validator;

import com.mysql.cj.util.StringUtils;
import com.sedlacek.quiz.dto.UserDto;
import com.sedlacek.quiz.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static com.sedlacek.quiz.utils.Constants.EMAIL_ALREADY_EXISTS;
import static com.sedlacek.quiz.utils.Constants.USERNAME_ALREADY_EXISTS;

public class UserValidator extends Validator<UserDto> {
    private static final String EMPTY_USERNAME = "Uživatelské jméno je povinné pole!";
    private static final String EMPTY_PASSWORD = "Heslo je povinné pole!";
    private static final String EMPTY_EMAIL = "Email je povinné pole!";
    private final List<String> messages = new ArrayList<>();
    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void validate(UserDto userDto) {
        validateUserRegisterBody(userDto);
        validateUserDuplicities(userDto);
    }

    private void validateUserRegisterBody(UserDto userDto) {
        if (StringUtils.isNullOrEmpty(userDto.getUsername())) {
            this.messages.add(EMPTY_USERNAME);
        }
        if (StringUtils.isNullOrEmpty(userDto.getPassword())) {
            this.messages.add(EMPTY_PASSWORD);
        }
        if (StringUtils.isNullOrEmpty(userDto.getEmail())) {
            this.messages.add(EMPTY_EMAIL);
        }
    }

    private void validateUserDuplicities(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            this.messages.add(USERNAME_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            this.messages.add(EMAIL_ALREADY_EXISTS);
        }
    }

    public List<String> getMessages() {
        List<String> errorMessages = new ArrayList<>(messages);
        this.messages.clear();
        return errorMessages;
    }
}
