package com.sedlacek.quiz.service.impl;

import com.sedlacek.quiz.dto.EditUserDto;
import com.sedlacek.quiz.dto.LoginResponseDto;
import com.sedlacek.quiz.dto.UserDto;
import com.sedlacek.quiz.entity.EntityBase;
import com.sedlacek.quiz.entity.User;
import com.sedlacek.quiz.exception.ResourceNotFoundException;
import com.sedlacek.quiz.exception.ValidationException;
import com.sedlacek.quiz.repository.UserRepository;
import com.sedlacek.quiz.service.UserService;
import com.sedlacek.quiz.utils.Constants;
import com.sedlacek.quiz.validator.UserValidator;
import com.sun.istack.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final UserValidator validator;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    public UserServiceImpl(UserRepository userRepository, JavaMailSender javaMailSender, UserValidator validator) {
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
        this.validator = validator;
    }


    @Override
    public String registerNewUser(@NotNull UserDto userDTO) {
        try {
            validator.validate(userDTO);
        } catch (ValidationException e) {
            return e.getMessage();
        }

        User user = EntityBase.convert(userDTO, User.class);
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        sendConfirmationEmail(user.getEmail(), user.getUsername());
        return Constants.USER + userDTO.getUsername() + Constants.REGISTRATION_SUCCESSFUL;
    }

    @Override
    public List<User> getAllUsersOrderByExp() {
        return userRepository.findAllByOrderByExpDesc();
    }

    @Override
    public ResponseEntity<LoginResponseDto> loginUser(@NotNull UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername());

        if (encoder.matches(userDto.getPassword(), user.getPassword())) {
            UserDto responseUser = EntityBase.convert(user, UserDto.class);
            return ResponseEntity.ok(new LoginResponseDto(responseUser, Constants.LOGIN_SUCCESSFUL));
        }
        return ResponseEntity.badRequest().body(new LoginResponseDto(null, Constants.WRONG_USERNAME_OR_PASSWORD));
    }

    @Override
    public User getUserById(@NotNull long id) throws ResourceNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_WITH_ID + id + Constants.NOT_FOUND));
    }

    @Override
    public String updateUser(long id, EditUserDto editUserDto) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_WITH_ID + id + Constants.NOT_FOUND));

        if (!encoder.matches(editUserDto.getPassword(), user.getPassword())) {
            return Constants.WRONG_PASSWORD;
        }
        if (!user.getUsername().equals(editUserDto.getUser().getUsername())) {
            if (userRepository.existsByUsername(editUserDto.getUser().getUsername())) {
                return Constants.USERNAME_ALREADY_EXISTS;
            }
            user.setUsername(editUserDto.getUser().getUsername());

        }
        if (!user.getPassword().equals(editUserDto.getUser().getPassword())) {
            user.setPassword(encoder.encode(editUserDto.getUser().getPassword()));

        }
        if (!user.getEmail().equals(editUserDto.getUser().getEmail())) {
            if (userRepository.existsByEmail(editUserDto.getUser().getEmail())) {
                return Constants.EMAIL_ALREADY_EXISTS;
            }
            user.setEmail(editUserDto.getUser().getEmail());
        }
        userRepository.save(user);
        return Constants.CHANGE_SUCCESSFUL;
    }

    @Override
    public String deleteUser(long id, String password) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_WITH_ID + id + Constants.NOT_FOUND));

        if (encoder.matches(password, user.getPassword())) {
            userRepository.delete(user);

            return Constants.USER_DELETED;
        }
        return Constants.WRONG_PASSWORD;
    }

    private void sendConfirmationEmail(String email, String username) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject(Constants.REGISTRATION_CONFIRMATION);
        message.setFrom(Constants.QUIZ_EMAIL_ADDRESS);
        message.setText("Ahoj " + username
                + ",\n\nděkujeme za registraci do hry Kvíz. Přejeme hodně štěstí a pevné nervy ve hře." +
                "\n\nS pozdravem\nQuiz Game development team");

        javaMailSender.send(message);
    }
}
