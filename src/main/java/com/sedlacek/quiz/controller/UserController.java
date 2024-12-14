package com.sedlacek.quiz.controller;

import com.sedlacek.quiz.dto.EditUserDto;
import com.sedlacek.quiz.dto.LoginResponseDto;
import com.sedlacek.quiz.dto.UserDto;
import com.sedlacek.quiz.entity.EntityBase;
import com.sedlacek.quiz.entity.User;
import com.sedlacek.quiz.exception.ResourceNotFoundException;
import com.sedlacek.quiz.service.UserService;
import com.sedlacek.quiz.utils.Constants;
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
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
        String result = userService.registerNewUser(userDto);
        if (result.contains(Constants.REGISTRATION_SUCCESSFUL)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("/leaderboards")
    public ResponseEntity<List<UserDto>> getLeaderboards() {
        List<User> users = userService.getAllUsersOrderByExp();
        List<UserDto> usersDto = EntityBase.convertAll(users, UserDto.class);
        return ResponseEntity.ok(usersDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@RequestBody UserDto userDto) {
        return userService.loginUser(userDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable(name = "id") long id) throws ResourceNotFoundException {
        User user = userService.getUserById(id);
        UserDto userDto = EntityBase.convert(user, UserDto.class);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable(name = "id") long id,
                                                 @RequestBody EditUserDto editUserDto)
            throws ResourceNotFoundException {
        String result = userService.updateUser(id, editUserDto);
        if (Constants.CHANGE_SUCCESSFUL.equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "id") long id,
                                                 @RequestParam(name = "password") String password)
            throws ResourceNotFoundException {
        String result = userService.deleteUser(id, password);
        if (Constants.USER_DELETED.equals(result)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
}

