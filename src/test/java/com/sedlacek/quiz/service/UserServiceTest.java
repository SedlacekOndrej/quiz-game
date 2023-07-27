package com.sedlacek.quiz.service;

import com.sedlacek.quiz.dto.LoginResponseDto;
import com.sedlacek.quiz.dto.ResponseMessageDto;
import com.sedlacek.quiz.dto.UserDto;
import com.sedlacek.quiz.entity.EntityBase;
import com.sedlacek.quiz.entity.User;
import com.sedlacek.quiz.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {
    private UserRepository fakeUserRepository;

    private UserService userService;

    private UserDto user;

    @BeforeEach
    public void init() {
        fakeUserRepository = mock(UserRepository.class);
        JavaMailSender fakeMailSender = mock(JavaMailSender.class);

        userService = new UserService(fakeUserRepository, fakeMailSender);

        user = new UserDto(OffsetDateTime.now(), 1L, "TestUser", "password123", "TestUser@gmail.com",
                1, 0L, 0, 0, 0.00, new ArrayList<>());
    }

    @Test
    void registerNewUser_StatusOk() {
        ResponseEntity<ResponseMessageDto> response = userService.registerNewUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals("Uživatel " + user.getUsername() + " úspěšně zaregistrován", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void registerNewUser_UsernameAlreadyExists_StatusBadRequest() {
        UserDto newUser = new UserDto(OffsetDateTime.now(), 1L, "TestUser", "password123", "NewTestUser@gmail.com",
                1, 0L, 0, 0, 0.00, new ArrayList<>());

        when(fakeUserRepository.existsByUsername(argThat(username -> username.equals(user.getUsername())))).thenReturn(true);

        ResponseEntity<ResponseMessageDto> response = userService.registerNewUser(newUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertEquals("Účet s tímto uživatelským jménem již existuje", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void registerNewUser_EmailAlreadyExists_StatusBadRequest() {
        UserDto newUser = new UserDto(OffsetDateTime.now(), 1L, "TestUser", "password123", "TestUser@gmail.com",
                1, 0L, 0, 0, 0.00, new ArrayList<>());

        when(fakeUserRepository.existsByEmail(argThat(email -> email.equals(user.getEmail())))).thenReturn(true);

        ResponseEntity<ResponseMessageDto> response = userService.registerNewUser(newUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertEquals("Účet s tímto emailem již existuje", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void loginUser_StatusOk() {
        String hashedPassword = new BCryptPasswordEncoder().encode(user.getPassword());

        User userEntity = new User();

        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(hashedPassword);

        when(fakeUserRepository.findByUsername(any(String.class))).thenReturn(userEntity);

        ResponseEntity<LoginResponseDto> response = userService.loginUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Přihlášení proběhlo úspěšně", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void loginUser_UserDoesNotExist_StatusBadRequest() {
        ResponseEntity<LoginResponseDto> response = userService.loginUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertEquals("Špatné uživatelské jméno nebo heslo", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void loginUser_GivenWrongUsername_StatusBadRequest() {
        userService.registerNewUser(user);

        UserDto newUser = new UserDto(OffsetDateTime.now(), 1L, "NewTestUser", "password123", "TestUser@gmail.com",
                1, 0L, 0, 0, 0.00, new ArrayList<>());

        ResponseEntity<LoginResponseDto> response = userService.loginUser(newUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertEquals("Špatné uživatelské jméno nebo heslo", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void loginUser_GivenWrongPassword_StatusBadRequest() {
        userService.registerNewUser(user);

        UserDto newUser = new UserDto(OffsetDateTime.now(), 1L, "TestUser", "password", "TestUser@gmail.com",
                1, 0L, 0, 0, 0.00, new ArrayList<>());

        ResponseEntity<LoginResponseDto> response = userService.loginUser(newUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertEquals("Špatné uživatelské jméno nebo heslo", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void getUserById_StatusOk() {
        User userEntity = EntityBase.convert(user, User.class);

        when(fakeUserRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(userEntity));

        ResponseEntity<UserDto> response = userService.getUserById(user.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals("TestUser", Objects.requireNonNull(response.getBody()).getUsername());
    }

    @Test
    void getAllUsersOrderByExp_Ok() {
        UserDto newUser = new UserDto(OffsetDateTime.now(), 1L, "NewUser", "password456", "NewUser@gmail.com",
                1, 10L, 0, 0, 0.00, new ArrayList<>());

        userService.registerNewUser(user);
        userService.registerNewUser(newUser);

        List<UserDto> expectedUsersDto = new ArrayList<>();

        expectedUsersDto.add(newUser);
        expectedUsersDto.add(user);

        List<User> expectedUsers = new ArrayList<>();

        for (UserDto userDto : expectedUsersDto) {
            expectedUsers.add(EntityBase.convert(userDto, User.class));
        }

        when(fakeUserRepository.findAllByOrderByExpDesc()).thenReturn(expectedUsers);

        List<UserDto> users = userService.getAllUsersOrderByExp().getBody();

        assert users != null;

        assertEquals(2, users.size());

        assertEquals("NewUser", users.get(0).getUsername());
    }

    @Test
    void getAllUsersOrderByExp_ListIsEmpty_True() {
        List<UserDto> users = userService.getAllUsersOrderByExp().getBody();

        assert users != null;

        assertEquals(0, users.size());

        assertTrue(users.isEmpty());
    }
}