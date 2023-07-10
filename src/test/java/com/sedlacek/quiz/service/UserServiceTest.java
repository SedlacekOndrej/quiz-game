package com.sedlacek.quiz.service;

import com.sedlacek.quiz.dto.LoginResponseDto;
import com.sedlacek.quiz.dto.ResponseMessageDto;
import com.sedlacek.quiz.dto.UserDto;
import com.sedlacek.quiz.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    UserService userService;

    UserDto user;

    @BeforeEach
    public void init() {
        UserRepository fakeUserRepository = Mockito.mock(UserRepository.class);

        userService = new UserService(fakeUserRepository);

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
        userService.registerNewUser(user);

        UserDto newUser = new UserDto(OffsetDateTime.now(), 1L, "TestUser", "password123", "NewTestUser@gmail.com",
                1, 0L, 0, 0, 0.00, new ArrayList<>());

        ResponseEntity<ResponseMessageDto> response = userService.registerNewUser(newUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertEquals("Účet s tímto uživatelským jménem již existuje", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void registerNewUser_EmailAlreadyExists_StatusBadRequest() {
        userService.registerNewUser(user);

        UserDto newUser = new UserDto(OffsetDateTime.now(), 1L, "NewTestUser", "password123", "TestUser@gmail.com",
                1, 0L, 0, 0, 0.00, new ArrayList<>());

        ResponseEntity<ResponseMessageDto> response = userService.registerNewUser(newUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertEquals("Účet s tímto emailem již existuje", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void loginUser_StatusOk() {
        userService.registerNewUser(user);

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
        userService.registerNewUser(user);

        ResponseEntity<UserDto> response = userService.getUserById(user.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAllUsersOrderByExp_Ok() {
        userService.registerNewUser(user);

        UserDto newUser = new UserDto(OffsetDateTime.now(), 1L, "NewUser", "password456", "NewUser@gmail.com",
                1, 10L, 0, 0, 0.00, new ArrayList<>());

        userService.registerNewUser(newUser);

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