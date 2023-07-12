package com.sedlacek.quiz.controller;

import com.sedlacek.quiz.dto.LoginResponseDto;
import com.sedlacek.quiz.dto.ResponseMessageDto;
import com.sedlacek.quiz.dto.UserDto;
import com.sedlacek.quiz.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@SuppressWarnings("unused")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/registration")
    public ResponseEntity<ResponseMessageDto> registerUser(@RequestBody UserDto userDto) {
        return userService.registerNewUser(userDto);
    }

    @GetMapping("/leaderboards")
    public ResponseEntity<List<UserDto>> getLeaderboards() {
        return userService.getAllUsersOrderByExp();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@RequestBody UserDto userDto) {
        return userService.loginUser(userDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable (name = "id") long id) {
        return userService.getUserById(id);
    }
}

