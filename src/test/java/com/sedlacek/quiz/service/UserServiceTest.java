package com.sedlacek.quiz.service;

import com.sedlacek.quiz.dto.LoginResponseDto;
import com.sedlacek.quiz.dto.ResponseMessageDto;
import com.sedlacek.quiz.dto.UserDto;
import com.sedlacek.quiz.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

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
        assertEquals(userService.registerNewUser(user), ResponseEntity.ok(new ResponseMessageDto("Uživatel " + user.getUsername() + " úspěšně zaregistrován")));
    }

    @Test
    void registerNewUser_UsernameAlreadyExists_StatusBadRequest() {
        userService.registerNewUser(user);

        UserDto newUser = new UserDto(OffsetDateTime.now(), 1L, "TestUser", "password123", "NewTestUser@gmail.com",
                1, 0L, 0, 0, 0.00, new ArrayList<>());

        assertEquals(userService.registerNewUser(newUser), ResponseEntity.badRequest().body(new ResponseMessageDto("Účet s tímto uživatelským jménem již existuje")));
    }

    @Test
    void registerNewUser_EmailAlreadyExists_StatusBadRequest() {
        userService.registerNewUser(user);

        UserDto newUser = new UserDto(OffsetDateTime.now(), 1L, "NewTestUser", "password123", "TestUser@gmail.com",
                1, 0L, 0, 0, 0.00, new ArrayList<>());

        assertEquals(userService.registerNewUser(newUser), ResponseEntity.badRequest().body(new ResponseMessageDto("Účet s tímto emailem již existuje")));
    }

    @Test
    void loginUser_StatusOk() {
        userService.registerNewUser(user);

        assertEquals(userService.loginUser(user), ResponseEntity.ok(new LoginResponseDto(user, "Přihlášení proběhlo úspěšně")));
    }

    @Test
    void loginUser_UserDoesNotExist_StatusBadRequest() {
        assertEquals(userService.loginUser(user), ResponseEntity.badRequest().body(new LoginResponseDto(null, "Špatné uživatelské jméno nebo heslo")));
    }

    @Test
    void loginUser_GivenWrongUsername_StatusBadRequest() {
        userService.registerNewUser(user);

        UserDto newUser = new UserDto(OffsetDateTime.now(), 1L, "NewTestUser", "password123", "TestUser@gmail.com",
                1, 0L, 0, 0, 0.00, new ArrayList<>());

        assertEquals(userService.loginUser(newUser), ResponseEntity.badRequest().body(new LoginResponseDto(null, "Špatné uživatelské jméno nebo heslo")));
    }

    @Test
    void loginUser_GivenWrongPassword_StatusBadRequest() {
        userService.registerNewUser(user);

        UserDto newUser = new UserDto(OffsetDateTime.now(), 1L, "TestUser", "password", "TestUser@gmail.com",
                1, 0L, 0, 0, 0.00, new ArrayList<>());

        assertEquals(userService.loginUser(newUser), ResponseEntity.badRequest().body(new LoginResponseDto(null, "Špatné uživatelské jméno nebo heslo")));
    }

    @Test
    void getUserById_StatusOk() {
        userService.registerNewUser(user);

        assertEquals(userService.getUserById(user.getId()), ResponseEntity.ok(user));
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
    }
}