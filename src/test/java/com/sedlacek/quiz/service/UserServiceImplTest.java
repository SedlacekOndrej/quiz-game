package com.sedlacek.quiz.service;

import com.sedlacek.quiz.dto.LoginResponseDto;
import com.sedlacek.quiz.dto.UserDto;
import com.sedlacek.quiz.entity.EntityBase;
import com.sedlacek.quiz.entity.User;
import com.sedlacek.quiz.exception.ResourceNotFoundException;
import com.sedlacek.quiz.repository.UserRepository;
import com.sedlacek.quiz.service.impl.UserServiceImpl;
import com.sedlacek.quiz.utils.Constants;
import com.sedlacek.quiz.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class UserServiceImplTest {
    @Mock
    private UserServiceImpl userServiceImpl;
    @InjectMocks
    private UserService userService;
    private UserRepository fakeUserRepository;
    private UserValidator fakeUserValidator;
    private UserDto user;

    @BeforeEach
    public void init() {
        fakeUserRepository = mock(UserRepository.class);
        fakeUserValidator = mock(UserValidator.class);
        JavaMailSender fakeMailSender = mock(JavaMailSender.class);

        userService = new UserServiceImpl(fakeUserRepository, fakeMailSender, fakeUserValidator);

        user = new UserDto(OffsetDateTime.now(), 1L, "TestUser", "password123", "TestUser@gmail.com",
                1, 0L, 0, 0, 0.00, new ArrayList<>());
    }

    @Test
    void registerNewUser_StatusOk() {
        String response = userService.registerNewUser(user);

        assertEquals(Constants.USER + user.getUsername() + Constants.REGISTRATION_SUCCESSFUL, response);
    }

    @Test
    void registerNewUser_UsernameAlreadyExists_StatusBadRequest() {
        UserDto newUser = new UserDto(OffsetDateTime.now(), 1L, "TestUser", "password123", "NewTestUser@gmail.com",
                1, 0L, 0, 0, 0.00, new ArrayList<>());

        when(fakeUserRepository.existsByUsername(argThat(username -> username.equals(user.getUsername())))).thenReturn(true);

        String response = userService.registerNewUser(newUser);

        assertEquals(Constants.USERNAME_ALREADY_EXISTS, response);
    }

    @Test
    void registerNewUser_EmailAlreadyExists_StatusBadRequest() {
        UserDto newUser = new UserDto(OffsetDateTime.now(), 1L, "TestUser", "password123", "TestUser@gmail.com",
                1, 0L, 0, 0, 0.00, new ArrayList<>());

        when(fakeUserRepository.existsByEmail(argThat(email -> email.equals(user.getEmail())))).thenReturn(true);

        String response = userService.registerNewUser(newUser);

        assertEquals(Constants.EMAIL_ALREADY_EXISTS, response);
    }

    @Test
    void loginUser_StatusOk() {
        String hashedPassword = new BCryptPasswordEncoder().encode(user.getPassword());

        User userEntity = new User();

        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(hashedPassword);

        when(fakeUserRepository.findByUsername(user.getUsername())).thenReturn(userEntity);

        ResponseEntity<LoginResponseDto> response = userService.loginUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Přihlášení proběhlo úspěšně", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void loginUser_UserDoesNotExist_StatusBadRequest() {
        when(fakeUserRepository.findByUsername("TestUser")).thenReturn(EntityBase.convert(user, User.class));

        ResponseEntity<LoginResponseDto> response = userService.loginUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertEquals("Špatné uživatelské jméno nebo heslo", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void loginUser_GivenWrongUsername_StatusBadRequest() {
        when(fakeUserRepository.findByUsername("NewTestUser")).thenReturn(EntityBase.convert(user, User.class));

        userService.registerNewUser(user);

        UserDto newUser = new UserDto(OffsetDateTime.now(), 1L, "NewTestUser", "password123", "TestUser@gmail.com",
                1, 0L, 0, 0, 0.00, new ArrayList<>());

        ResponseEntity<LoginResponseDto> response = userService.loginUser(newUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertEquals("Špatné uživatelské jméno nebo heslo", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void loginUser_GivenWrongPassword_StatusBadRequest() {
        when(fakeUserRepository.findByUsername("TestUser")).thenReturn(EntityBase.convert(user, User.class));

        userService.registerNewUser(user);

        UserDto newUser = new UserDto(OffsetDateTime.now(), 1L, "TestUser", "password", "TestUser@gmail.com",
                1, 0L, 0, 0, 0.00, new ArrayList<>());

        ResponseEntity<LoginResponseDto> response = userService.loginUser(newUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertEquals("Špatné uživatelské jméno nebo heslo", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void getUserById_StatusOk() throws ResourceNotFoundException {
        User userEntity = EntityBase.convert(user, User.class);

        when(fakeUserRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(userEntity));

        User user = userService.getUserById(this.user.getId());


        assertEquals("TestUser", Objects.requireNonNull(user).getUsername());
    }

    @Test
    void getAllUsersOrderByExp_Ok() {
        UserDto newUser = new UserDto(OffsetDateTime.now(), 1L, "NewUser", "password456",
                "NewUser@gmail.com", 1, 10L, 0, 0, 0.00, new ArrayList<>());

        userService.registerNewUser(user);
        userService.registerNewUser(newUser);

        List<UserDto> expectedUsersDto = List.of(user, newUser);

        List<User> expectedUsers = EntityBase.convertAll(expectedUsersDto, User.class);

        when(fakeUserRepository.findAllByOrderByExpDesc()).thenReturn(expectedUsers);

        List<User> users = userService.getAllUsersOrderByExp();

        assert users != null;

        assertEquals(2, users.size());

        assertEquals("TestUser", users.get(0).getUsername());
    }

    @Test
    void getAllUsersOrderByExp_ListIsEmpty_True() {
        List<User> users = userService.getAllUsersOrderByExp();

        assert users != null;

        assertEquals(0, users.size());

        assertTrue(users.isEmpty());
    }
}