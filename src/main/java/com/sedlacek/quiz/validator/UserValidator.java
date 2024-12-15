package com.sedlacek.quiz.validator;

import com.mysql.cj.util.StringUtils;
import com.sedlacek.quiz.dto.UserDto;
import com.sedlacek.quiz.repository.UserRepository;

import static com.sedlacek.quiz.utils.Constants.*;

public class UserValidator extends Validator<UserDto> {
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
            throw new IllegalArgumentException(EMPTY_USERNAME);
        }
        if (StringUtils.isNullOrEmpty(userDto.getPassword())) {
            throw new IllegalArgumentException(EMPTY_PASSWORD);
        }
        if (StringUtils.isNullOrEmpty(userDto.getEmail())) {
            throw new IllegalArgumentException(EMPTY_EMAIL);
        }
    }

    private void validateUserDuplicities(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new IllegalArgumentException(USERNAME_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException(EMAIL_ALREADY_EXISTS);
        }
    }
}
