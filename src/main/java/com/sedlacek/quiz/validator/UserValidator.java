package com.sedlacek.quiz.validator;

import com.mysql.cj.util.StringUtils;
import com.sedlacek.quiz.dto.UserDto;
import com.sedlacek.quiz.exception.ValidationException;
import com.sedlacek.quiz.repository.UserRepository;

import static com.sedlacek.quiz.utils.Constants.*;

public class UserValidator extends Validator<UserDto> {
    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void validate(UserDto userDto) throws ValidationException {
        validateUserRegisterBody(userDto);
        validateUserDuplicities(userDto);
    }

    private void validateUserRegisterBody(UserDto userDto) throws ValidationException {
        if (StringUtils.isNullOrEmpty(userDto.getUsername())) {
            throw new ValidationException(EMPTY_USERNAME);
        }
        if (StringUtils.isNullOrEmpty(userDto.getPassword())) {
            throw new ValidationException(EMPTY_PASSWORD);
        }
        if (StringUtils.isNullOrEmpty(userDto.getEmail())) {
            throw new ValidationException(EMPTY_EMAIL);
        }
    }

    private void validateUserDuplicities(UserDto userDto) throws ValidationException {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new ValidationException(USERNAME_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ValidationException(EMAIL_ALREADY_EXISTS);
        }
    }
}
